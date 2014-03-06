/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
//Create the blackboardPortlet variable if it does not already exist
var blackboardPortlet = blackboardPortlet || {};

/*
 * Switch libraries to extreme noConflict mode, keeping a reference to it in the
 * blackboardPortlet variable if one doesn't already exist
 */

if (!blackboardPortlet.jQuery) {
   blackboardPortlet.jQuery = jQuery.noConflict(true);
} else {
   jQuery.noConflict(true);
}

if (!blackboardPortlet.Backbone) {
   blackboardPortlet.Backbone = Backbone.noConflict();
} else {
   Backbone.noConflict();
}

if (!blackboardPortlet._) {
   blackboardPortlet._ = _.noConflict();
   blackboardPortlet._.templateSettings = {
      interpolate : /{{=(.+?)}}/g,
      evaluate : /{{(.+?)}}/g
   };
} else {
   _.noConflict();
}

(function($, Backbone, _) {
   'use strict';

   blackboardPortlet.showTooltip = function(selector) {
      $(selector).tooltip({
         position : {
            my : "center bottom-20",
            at : "center top",
            using : function(position, feedback) {
               $(this).css(position);
               $("<div>").addClass("bbcArrow").addClass(feedback.vertical).addClass(feedback.horizontal).appendTo(this);
            }
         },
         show: false,
         hide: false,
         tooltipClass: "bbc-ui-tooltip",
         content : function() {
            return $(this).prop('title');
         }
      });
   };

   blackboardPortlet.initParticipantAutoComplete = function(bbOpts) {
      var uniqueIdInput = $(bbOpts.uniqueIdSelector);
      var nameInput = $(bbOpts.nameSelector);
      var emailInput = $(bbOpts.emailSelector);
      
      var searchCache = {
         name: {},
         email: {}
      };
      
      var searchMaker = function(searchType) {
         return function(request, response) {
            if (bbOpts.currentSearchRequest != null) {
               bbOpts.currentSearchRequest.abort();
               bbOpts.currentSearchRequest = null;
            }
            
            var term = request.term;
            if (term in searchCache[searchType]) {
               response( searchCache[searchType][term] );
               return;
            }
            
            var data = {};
            data[searchType] = term;
            
            $.log("Search for: " + searchType + "=" + term);
            
            bbOpts.currentSearchRequest = $.ajax({
               url: bbOpts.searchForParticipantsUrl,
               type: 'POST',
               timeout: 10000,
               data: data,
               success: function( data, status, xhr ) {
                  bbOpts.currentSearchRequest = null;
                  
                  data = data.result;
                  searchCache[searchType][term] = data;
                  response( data );
               }
            });
         };
      };
      
      var setupAutocomplete = function(input, searchType) {
         input.autocomplete({
            source: searchMaker(searchType),
            minLength: 2,
            dataType: 'json',
            focus: function( event, ui ) {
               uniqueIdInput.val( ui.item.uniqueId );
               nameInput.val( ui.item.displayName );
               emailInput.val( ui.item.email );
               return false;
            },
            select: function( event, ui ) {
               uniqueIdInput.val( ui.item.uniqueId );
               nameInput.val( ui.item.displayName );
               emailInput.val( ui.item.email );
               
               $("input.ui-autocomplete-loading").removeClass("ui-autocomplete-loading");
               
               return false;
            }
         }).data( "ui-autocomplete" )._renderItem = function( ul, item ) {
            return $( "<li>" )
              .append( "<a>" + item.displayName + "<br>(" + item.email + ")</a>" )
              .appendTo( ul );
         };
         
         input.keypress(function() {
            uniqueIdInput.val( "" );
         });
      };
      
      setupAutocomplete(nameInput, "name");
      setupAutocomplete(emailInput, "email");
   };
   
   blackboardPortlet.initMediaFileBackbone = function(bbOpts) {
      blackboardPortlet.mediaFile = blackboardPortlet.mediaFile || {};
      blackboardPortlet.mediaFile.model = blackboardPortlet.mediaFile.model || {};
      blackboardPortlet.mediaFile.view = blackboardPortlet.mediaFile.view || {};
      
      var bbNamespace = blackboardPortlet.mediaFile;
      
      /**
       * Centralized sync method
       */
      bbNamespace.sync = function(type) {
         return function(method, model, options) {
            // We don't own options so we shouldn't modify it,
            // but we can do whatever we want to a clone.
            options = _(options).clone();

            var params = {
               type : 'POST',
            };

            $.log("-----SYNC-----");
            $.log(type);
            $.log(method);
            $.log(model);
            $.log(options);
            


            if (type === "MediaFilesList") {
               if (method == "read") {
                  params.url = bbOpts.getMediaFilesUrl;

                  params.data = {
                     sessionId : bbOpts.sessionId
                  };

                  // Replace options.success with a wrapper.
                  var success = options.success;
                  options.success = function(data, textStatus, jqXHR) {
                     if (success) {
                        var parsedData = [];

                        $.each(data.multimedias, function(idx, mediaFile) {
                           parsedData.push({
                              id : mediaFile.multimediaId,
                              name : mediaFile.filename
                           });
                        });

                        success(parsedData, textStatus, jqXHR);
                     }
                  };
               }
            }
            
            if (params.url != null) {
               if (bbOpts.onSyncAjax != null) {
                  bbOpts.onSyncAjax(type, method, model, options);
               }
               $.ajax(_.extend(params, options));
            }
            
            $.log("--------------");
         };
      };

      bbNamespace.model.MediaFile = Backbone.Model.extend({
         sync : bbNamespace.sync("MediaFile"),
         toJSON : function() {
            var json = Backbone.Model.prototype.toJSON.apply(this, arguments);
            json.cid = this.cid;
            return json;
         }
      });

      bbNamespace.model.MediaFilesList = Backbone.Collection.extend({
         model : bbNamespace.model.MediaFile,
         sync : bbNamespace.sync("MediaFilesList")
      });

      bbNamespace.view.MediaFileView = Backbone.View.extend({
         tagName : "tr",
         className : "mediFile-row",
         template : _.template($(bbOpts.mediaFileTemplateSelector).html()),

         initialize : function() {
            _.bindAll(this);

            this.listenTo(this.model, 'change', this.render);
            this.listenTo(this.model, 'destroy', this.remove);
         },

         render : function() {
            $(this.el).html(this.template(this.model.toJSON()));
            return this;
         }
      });

      bbNamespace.view.MediaFilesView = Backbone.View.extend({
         el : $(bbOpts.mediaFilesViewSelector),

         initialize : function(options) {
            this.listenTo(this.model, 'add', this.addOne);
            this.listenTo(this.model, 'remove', this.remove);
            this.listenTo(this.model, 'reset', this.addAll);
            this.listenTo(this.model, 'error', this.error);

//            this.uniqueIdInput = this.$("input[name='newUniqueId']");
//            this.nameInput = this.$("input[name='newName']");
//            this.emailInput = this.$("input[name='newEmail']");
//            this.modSelect = this.$("select[name='newModerator']");
         },
         events : {
            "click button[name='deleteSelected']" : "deleteSelected",
            "click button[name='uploadFile']" : "uploadFile"
         },
         render : function() {
            var container = document.createDocumentFragment();
            _.forEach(this.model.models, function(mediaFile) {
               var view = new bbNamespace.view.MediaFileView({
                  model : mediaFile
               });
               container.appendChild(view.render().el);
            }, this);
            this.$("tbody").html(container);

            return this;
         },
//         error : function(model, resp, options) {
//            //TODO the upload needs to happen syncronously
//            this.model.remove(model);
//            this.uniqueIdInput.val(model.get("uniqueId"));
//            this.nameInput.val(model.get("name"));
//            this.emailInput.val(model.get("email"));
//            this.modSelect.val(model.get("moderator") + "");
//
//            var responseData = $.parseJSON(resp.responseText);
//            $.log(responseData);
//
//            var that = this;
//            $.each(responseData.fieldErrors, function(key, value) {
//               var errorDiv = that.$("div.ajaxerror." + key);
//               errorDiv.text(value);
//               errorDiv.show();
//            });
//         },
         uploadFile : function(e) {
            this.$("div.ajaxerror").hide();

            var uniqueId = this.uniqueIdInput.val();
            var newName = this.nameInput.val();
            var newEmail = this.emailInput.val();
            var newModerator = this.modSelect.find("option:selected").val();

            if (!newName || !newEmail) {
               return;
            }

            this.model.create({
               uniqueId : uniqueId,
               name : newName,
               email : newEmail,
               moderator : newModerator.toUpperCase() === 'TRUE'
            });

            this.nameInput.val('');
            this.emailInput.val('');
         },

         deleteSelected : function(e) {
            var data = {
               sessionId : bbOpts.sessionId,
               id : []
            };

            var mdl = this.model;

            var selectedFiles = this.$("input[name='file_select']:checked");
            selectedFiles.each(function(idx, fileSelect) {
               var cid = $(fileSelect).val();
               var fileModel = mdl.get(cid);
               data.id.push(fileModel.get("id"));
               fileModel.destroy();
            });

            $.ajax({
               type : 'POST',
               url : bbOpts.deleteMediaFileUrl,
               data : data,
               traditional : true
            });
         },

         // Add a single setting item to the list by creating a view for it
         addOne : function(participant) {
            var view = new bbNamespace.view.MediaFileView({
               model : participant
            });
            this.$("tbody").append(view.render().el);
         },

         // Add all items in the setting collection at once.
         addAll : function() {
            this.model.each(this.addOne, this);
         },

         remove : function(e) {
            this.render();
         }
      });

      var mediaFilesList = new bbNamespace.model.MediaFilesList();

      new bbNamespace.view.MediaFilesView({
         model : mediaFilesList
      });

      mediaFilesList.fetch();
   };

   blackboardPortlet.initParticipantBackbone = function(bbOpts) {
      blackboardPortlet.participant = blackboardPortlet.participant || {};
      blackboardPortlet.participant.model = blackboardPortlet.participant.model || {};
      blackboardPortlet.participant.view = blackboardPortlet.participant.view || {};
      
      var bbNamespace = blackboardPortlet.participant;

      /**
       * Centralized sync method
       */
      bbNamespace.sync = function(type) {
         return function(method, model, options) {
            // We don't own options so we shouldn't modify it,
            // but we can do whatever we want to a clone.
            options = _(options).clone();

            var params = {
               type : 'POST',
            };

            $.log("-----SYNC-----");
            $.log(type);
            $.log(method);
            $.log(model);
            $.log(options);

            if (type === "ParticipantsList") {
               if (method == "read") {
                  params.url = bbOpts.getParticipantsUrl;

                  params.data = {
                     sessionId : bbOpts.sessionId
                  };

                  // Replace options.success with a wrapper.
                  var success = options.success;
                  options.success = function(data, textStatus, jqXHR) {
                     if (success) {
                        var parsedData = [];

                        $.each(data.sessionChairs, function(idx, user) {
                           parsedData.push({
                              id : user.userId,
                              name : user.displayName,
                              email : user.email,
                              moderator : true
                           });
                        });

                        $.each(data.sessionNonChairs, function(idx, user) {
                           parsedData.push({
                              id : user.userId,
                              name : user.displayName,
                              email : user.email,
                              moderator : false
                           });
                        });

                        success(parsedData, textStatus, jqXHR);
                     }
                  };
               }
            } else if (type === "Participant") {
               params.data = model.toJSON();
               params.data.sessionId = bbOpts.sessionId;

               if (method == "create") {
                  params.url = bbOpts.addParticipantUrl;

                  // Replace options.success with a wrapper.
                  var success = options.success;
                  options.success = function(data, textStatus, jqXHR) {
                     if (success) {
                        data = {
                           id : data.participant.userId,
                           name : data.participant.displayName,
                           email : data.participant.email,
                           moderator : data.addParticipantForm.moderator
                        };

                        success(data, textStatus, jqXHR);
                     }
                  };
               } else if (method == "update") {
                  params.url = bbOpts.updateParticipantUrl;

                  // Replace options.success with a wrapper.
                  var success = options.success;
                  options.success = function(data, textStatus, jqXHR) {
                     if (success) {
                        data = {
                           id : data.participant.userId,
                           name : data.participant.displayName,
                           email : data.participant.email,
                           moderator : data.updateParticipantForm.moderator
                        };

                        success(data, textStatus, jqXHR);
                     }
                  };

               }
            }

            if (params.url != null) {
               if (bbOpts.onSyncAjax != null) {
                  bbOpts.onSyncAjax(type, method, model, options);
               }
               $.ajax(_.extend(params, options));
            }

            $.log("--------------");
         };
      };

      bbNamespace.model.Participant = Backbone.Model.extend({
         sync : bbNamespace.sync("Participant"),
         toJSON : function() {
            var json = Backbone.Model.prototype.toJSON.apply(this, arguments);
            json.cid = this.cid;
            return json;
         }
      });

      bbNamespace.model.ParticipantsList = Backbone.Collection.extend({
         model : bbNamespace.model.Participant,
         sync : bbNamespace.sync("ParticipantsList")
      });

      bbNamespace.view.ParticipantView = Backbone.View.extend({
         tagName : "tr",
         className : "participant-row",
         template : _.template($(bbOpts.participantTemplateSelector).html()),

         events : {
            "change select.moderator" : "moderatorChange"
         },

         initialize : function() {
            _.bindAll(this);

            this.listenTo(this.model, 'change', this.render);
            this.listenTo(this.model, 'destroy', this.remove);
         },

         render : function() {
            $(this.el).html(this.template(this.model.toJSON()));
            return this;
         },

         moderatorChange : function() {
            var isModerator = this.$("select.moderator option:selected").val().toUpperCase() === 'TRUE';
            this.model.save({
               moderator : isModerator
            });
         }
      });

      bbNamespace.view.ParticipantsView = Backbone.View.extend({
         el : $(bbOpts.participantsViewSelector),

         initialize : function(options) {
            this.listenTo(this.model, 'add', this.addOne);
            this.listenTo(this.model, 'remove', this.remove);
            this.listenTo(this.model, 'reset', this.addAll);
            this.listenTo(this.model, 'error', this.error);

            this.uniqueIdInput = this.$("input[name='newUniqueId']");
            this.nameInput = this.$("input[name='newName']");
            this.emailInput = this.$("input[name='newEmail']");
            this.modSelect = this.$("select[name='newModerator']");
         },
         events : {
            "click button[name='deleteSelected']" : "deleteSelected",
            "click button[name='addParticipant']" : "addParticipant"
         },
         render : function() {
            var container = document.createDocumentFragment();
            _.forEach(this.model.models, function(participant) {
               var view = new bbNamespace.view.ParticipantView({
                  model : participant
               });
               container.appendChild(view.render().el);
            }, this);
            this.$("tbody").html(container);

            return this;
         },
         error : function(model, resp, options) {
            this.model.remove(model);
            this.uniqueIdInput.val(model.get("uniqueId"));
            this.nameInput.val(model.get("name"));
            this.emailInput.val(model.get("email"));
            this.modSelect.val(model.get("moderator") + "");

            var responseData = $.parseJSON(resp.responseText);
            $.log(responseData);

            var that = this;
            $.each(responseData.fieldErrors, function(key, value) {
               var errorDiv = that.$("div.ajaxerror." + key);
               errorDiv.text(value);
               errorDiv.show();
            });
         },
         addParticipant : function(e) {
            this.$("div.ajaxerror").hide();
            this.$("input.ui-autocomplete-loading").removeClass("ui-autocomplete-loading");

            var uniqueId = this.uniqueIdInput.val();
            var newName = this.nameInput.val();
            var newEmail = this.emailInput.val();
            var newModerator = this.modSelect.find("option:selected").val();

            if (!newName || !newEmail) {
               return;
            }

            this.model.create({
               uniqueId : uniqueId,
               name : newName,
               email : newEmail,
               moderator : newModerator.toUpperCase() === 'TRUE'
            });

            this.nameInput.val('');
            this.emailInput.val('');
         },

         deleteSelected : function(e) {
            var data = {
               sessionId : bbOpts.sessionId,
               id : []
            };

            var mdl = this.model;

            var selectedParticipants = this.$("input[name='participant_select']:checked");
            selectedParticipants.each(function(idx, participantSelect) {
               var cid = $(participantSelect).val();
               var participantModel = mdl.get(cid);
               data.id.push(participantModel.get("id"));
               participantModel.destroy();
            });

            $.ajax({
               type : 'POST',
               url : bbOpts.deleteParticipantUrl,
               data : data,
               traditional : true
            });
         },

         // Add a single setting item to the list by creating a view for it
         addOne : function(participant) {
            var view = new bbNamespace.view.ParticipantView({
               model : participant
            });
            this.$("tbody").append(view.render().el);
         },

         // Add all items in the setting collection at once.
         addAll : function() {
            this.model.each(this.addOne, this);
         },

         remove : function(e) {
            this.render();
         }
      });

      var participantsList = new bbNamespace.model.ParticipantsList();

      new bbNamespace.view.ParticipantsView({
         model : participantsList
      });

      participantsList.fetch();

   }
   
})(blackboardPortlet.jQuery, blackboardPortlet.Backbone, blackboardPortlet._);
















