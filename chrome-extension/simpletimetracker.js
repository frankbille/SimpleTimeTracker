/*
 * Copyright 2011 Frank Bille
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
STT = {
	// property
	account : null,

	// property
	spinner : null,

	// property
	timer : null,

	// property
	running : false,

	// property
	startTime : null,

	// property
	endTime : null,

	init : function() {
		this.initSettings();
		this.initSpinner();
		this.initTemplates();

		this.show();
	},

	initSettings : function() {
		var account = localStorage["stt_account"];
		if (typeof (account) === "string") {
			this.account = account;
		}
	},

	initSpinner : function() {
		var opts = {
			lines : 12,
			length : 30,
			width : 10,
			radius : 30,
			trail : 100,
			speed : 1,
			shadow : true
		};

		this.spinner = new Spinner(opts);
	},

	initTemplates : function() {
		$("#setupTemplate").template("setupTemplate");
		$("#timerTimeTemplate").template("timerTimeTemplate");
		$("#timerTemplate").template("timerTemplate");
	},

	hasAccount : function() {
		return this.account != null;
	},

	setAccount : function(account) {
		this.account = account;
		localStorage["stt_account"] = this.account;
	},

	show : function() {
		this.removeMainContent();

		if (this.hasAccount()) {
			this.showTimerView();
		} else {
			this.showSetupView();
		}
	},

	showSetupView : function() {
		var me = this;

		var data = {
			setupDescription : chrome.i18n.getMessage("setupDescription"),
			setupNewLinkText : chrome.i18n.getMessage("setupNewLink"),
			setupExistingLinkText : chrome.i18n.getMessage("setupExistingLink")
		};

		$("#mainView").delegate("#setupNewLink", "click", function() {
			me.showSpinner();

			$.ajax({
				type : "POST",
				url : "http://simpletimetrackerapp.appspot.com/createaccount",
				success : function(data) {
					me.setAccount(data.accountKey);

					me.show();

					me.hideSpinner();
				}
			});
		});

		this.setMainContent($.tmpl("setupTemplate", data));
	},

	showTimerView : function() {
		var me = this;

		me.showSpinner();

		$.ajax({
			type : "GET",
			url : "http://simpletimetrackerapp.appspot.com/time",
			username : me.account,
			success : function(data) {
				me.hideSpinner();
				
				if (typeof (data) === "string") {
					me.running = false;
				} else if (toString.call(data) === "[object Object]") {
					me.running = null == data.endTime;
					me.startTime = new Date(data.startTime);
					me.endTime = data.endTime != null ? new Date(data.endTime) : null;
				}

				var data = {
					time : me.createTimeLabel(),
					running : me.running,
					buttonLabel : me.running ? chrome.i18n
							.getMessage("stopTimer") : chrome.i18n
							.getMessage("startTimer")
				};

				$("#mainView").delegate("#timerButton", "click", function() {
					me.showSpinner();
					
					$.ajax({
						type : "POST",
						url : "http://simpletimetrackerapp.appspot.com/timer",
						username : me.account,
						success : function() {
							me.hideSpinner();
							
							me.show();
						}
					});
				});

				me.setMainContent($.tmpl("timerTemplate", data));

				me.clockTimer();
			}
		});

	},

	clockTimer : function() {
		var me = this;

		me.renderTime();

		if (me.running) {
			me.timer = setTimeout(function() {
				me.clockTimer();
			}, 1000);
		} else {
			if (me.timer != null) {
				clearTimeout(me.timer);
				me.timer = null;
			}
		}
	},

	createTimeLabel : function() {
		var me = this;
		var timeString;
		
		var time = me.startTime;
		if (time != null) {
			var endTime = me.endTime;
			if (endTime == null) {
				endTime = new Date();
			}
			
			var t1 = time.getTime();
			var t2 = endTime.getTime();
			var d = t2-t1;
			var s = Math.floor(d/1000);
			var m = Math.floor((d/(60*1000))%60);
			var h = Math.floor(d/(60*60*1000));
			
			timeString = (""+h).length == 1 ? "0"+h : h;
			if (false == me.running || s % 2 == 0) {
				timeString += ":";
			} else {
				timeString += " ";
			}
			timeString += (""+m).length == 1 ? "0"+m : m;
		} else {
			timeString = "00:00";
		}
		
		return timeString;
	},

	renderTime : function() {
		var me = this;

		var timeTmpl = $.tmplItem($(".time").get(0));
		timeTmpl.data = {
			time : me.createTimeLabel(),
			running : me.running
		};
		timeTmpl.update();
	},

	removeMainContent : function() {
		$("#mainView").empty();
	},

	setMainContent : function(content) {
		this.removeMainContent();
		$("#mainView").append(content);
	},

	showSpinner : function() {
		$("<div>").addClass("mask").appendTo($("body"));
		this.spinner.spin(document.body);
	},

	hideSpinner : function() {
		this.spinner.stop();
		$(".mask").remove();
	}

};
