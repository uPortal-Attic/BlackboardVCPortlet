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
 * Switch jQuery to extreme noConflict mode, keeping a reference to it in the coursesPortlet
 * variable if one doesn't already exist
 */
if (!blackboardPortlet.jQuery) {
	blackboardPortlet.jQuery = jQuery.noConflict(true);
}
else {
    jQuery.noConflict(true);
}

(function($, dl) {

dl.showTooltip = function (selector) {
	$(selector).tooltip({
      position: {
        my: "center bottom-20",
        at: "center top",
        using: function( position, feedback ) {
          $( this ).css( position );
          $( "<div>" )
            .addClass( "arrow" )
            .addClass( feedback.vertical )
            .addClass( feedback.horizontal )
            .appendTo( this );
        }
      },
	  content: function () {
          return $(this).prop('title');
      }
    });
}
})(blackboardPortlet.jQuery, blackboardPortlet);