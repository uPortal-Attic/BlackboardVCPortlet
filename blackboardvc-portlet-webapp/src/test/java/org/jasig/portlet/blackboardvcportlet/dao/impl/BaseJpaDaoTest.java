package org.jasig.portlet.blackboardvcportlet.dao.impl;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.aopalliance.intercept.MethodInvocation;
import org.jasig.jpa.BaseJpaDao;
import org.jasig.springframework.mockito.MockitoFactoryBean;
import org.junit.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaInterceptor;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;

/**
 * Base class for JPA based unit tests that want TX and entity manager support.
 * Also deletes all hibernate managed data from the database after each test execution 
 * 
 * @author Eric Dalquist
 */
public abstract class BaseJpaDaoTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    @SuppressWarnings("deprecation")
    protected JpaInterceptor jpaInterceptor;
    protected TransactionOperations transactionOperations;
    private EntityManager entityManager;

    @PersistenceContext(unitName = BaseJpaDao.PERSISTENCE_UNIT_NAME)
    public final void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    protected final EntityManager getEntityManager() {
        return this.entityManager;
    }
    
    @Autowired
    public final void setJpaInterceptor(@SuppressWarnings("deprecation") JpaInterceptor jpaInterceptor) {
        this.jpaInterceptor = jpaInterceptor;
    }

    @Autowired
    public void setTransactionOperations(TransactionOperations transactionOperations) {
        this.transactionOperations = transactionOperations;
    }

    /**
     * Deletes ALL entities from the database
     */
    @After
    public final void deleteAllEntities() {
        final EntityManager entityManager = getEntityManager();
        final EntityManagerFactory entityManagerFactory = entityManager.getEntityManagerFactory();
        final Metamodel metamodel = entityManagerFactory.getMetamodel();
        Set<EntityType<?>> entityTypes = new LinkedHashSet<EntityType<?>>(metamodel.getEntities());

        do {
            final Set<EntityType<?>> failedEntitieTypes = new HashSet<EntityType<?>>();
            
            for (final EntityType<?> entityType : entityTypes) {
                final String entityClassName = entityType.getBindableJavaType().getName();
                
                try {
                    this.executeInTransaction(new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            logger.trace("Purging all: " + entityClassName);
                            
                            final Query query = entityManager.createQuery("SELECT e FROM " + entityClassName + " AS e");
                            final List<?> entities = query.getResultList();
                            logger.trace("Found " + entities.size() + " " + entityClassName + " to delete");
                            for (final Object entity : entities) {
                                entityManager.remove(entity);
                            }      
                            
                            return null;
                        }
                    });
                }
                catch (DataIntegrityViolationException e) {
                    logger.trace("Failed to delete " + entityClassName + ". Must be a dependency of another entity");
                    failedEntitieTypes.add(entityType);
                }
            }
            
            entityTypes = failedEntitieTypes;
        } while (!entityTypes.isEmpty());
        
        
        //Reset all spring managed mocks after every test
        MockitoFactoryBean.resetAllMocks();
    }

    /**
     * Executes the callback inside of a {@link JpaInterceptor}.
     */
    @SuppressWarnings({ "unchecked", "deprecation" })
    public final <T> T execute(final Callable<T> callable) {
        try {
            return (T)this.jpaInterceptor.invoke(new MethodInvocationCallable<T>(callable));
        }
        catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException)e;
            }
            if (e instanceof Error) {
                throw (Error)e;
            }
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Executes the callback inside of a {@link JpaInterceptor} inside of a {@link TransactionCallback}
     */
    public final <T> T executeInTransaction(final Callable<T> callable) {
        return execute(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return transactionOperations.execute(new TransactionCallback<T>() {
                    @Override
                    public T doInTransaction(TransactionStatus status) {
                        try {
                            return callable.call();
                        }
                        catch (RuntimeException e) {
                            throw e;
                        }
                        catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
    }

    /**
     * Executes the callback in a new thread inside of a {@link JpaInterceptor}. Waits for the
     * Thread to return.
     */
    public final <T> T executeInThread(String name, final Callable<T> callable) {
        final List<RuntimeException> exception = new LinkedList<RuntimeException>();
        final List<T> retVal = new LinkedList<T>();
        
        final Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final T val = execute(callable);
                    retVal.add(val);
                }
                catch (Throwable e) {
                    if (e instanceof RuntimeException) {
                        exception.add((RuntimeException)e);                    
                    }
                    else {
                        exception.add(new RuntimeException(e));
                    }
                }
            }
        }, name);
        
        t2.start();
        try {
            t2.join();
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        
        if (exception.size() == 1) {
            throw exception.get(0);
        }
        
        return retVal.get(0);
    }

    private static final class MethodInvocationCallable<T> implements MethodInvocation {
        private final Callable<T> callable;

        private MethodInvocationCallable(Callable<T> callable) {
            this.callable = callable;
        }

        @Override
        public Object proceed() throws Throwable {
            return callable.call();
        }

        @Override
        public Object getThis() {
            throw new UnsupportedOperationException();
        }

        @Override
        public AccessibleObject getStaticPart() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object[] getArguments() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Method getMethod() {
            throw new UnsupportedOperationException();
        }
    }
}