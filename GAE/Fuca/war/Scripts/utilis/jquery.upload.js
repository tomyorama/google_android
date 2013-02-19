/*------------------------------------*/
//	Utilis - jquery.upload.js
//
//	depend on: plupload - http://www.plupload.com/documentation.php
/*------------------------------------*/

(function ($) {

	var control = function (element, options) {
		this.options = options;
		this._element = $(element);
		this._filesContainer = this._element.find('.u-upload-queue');
		this._error = this._element.find('.field-validation-error');

		// Fix engine url
		this.options.uploader.flash_swf_url = this.options.runtime_url + this.options.uploader.flash_swf_url;
		this.options.uploader.silverlight_xap_url = this.options.runtime_url + this.options.uploader.silverlight_xap_url;

		this.uploadButton = this._element.find('[data-u-upload-button-start="true"]');
		this.uploadButton
			.on('click', $.proxy(this._start, this))
			.hide();

		// Set container id
		var elId = this._element.attr('id');
		if (!elId) {
			elId = $.format('__upload_{0}', plupload.guid());
			this._element.attr('id', elId);
		}
		this.options.uploader.container = elId;

		setTimeout($.proxy(this._init, this), 10);
	};

	control.prototype = {
		_init: function () {
			if (this._element.is(':visible')) {
				this.uploader = new plupload.Uploader(this.options.uploader);
				this.uploader.init();

				this.uploader.bind('FilesAdded', $.proxy(this._filesAdded, this));
				this.uploader.bind('QueueChanged', $.proxy(this._queueChanged, this));
				this.uploader.bind('FileUploaded', $.proxy(this._fileUploaded, this));
				this.uploader.bind('UploadProgress', $.proxy(this._uploadProgress, this));
				this.uploader.bind('Error', $.proxy(this._uploadError, this));
			}
		},
		_queueChanged: function () {
			this._filesContainer.empty();
			this.queue = {};
			$.each(this.uploader.files, $.proxy(function (i, file) {
				var div = $($.format('<div class="u-upload-file"><span class="u-upload-file-name">{0} ({1})</span></div>', file.name, plupload.formatSize(file.size)));
				var progressBox = $('<span class="u-upload-progress" />');
				var progress = $('<span />').appendTo(progressBox);
				progress.width('0%');
				div.append(progressBox);
				this.queue[file.id] = { file: file, el: div, progress: progress };
				this._filesContainer.append(div);
			}, this));
			var uploadButtonVisible = !!this.uploader.files.length && this.uploader.state == plupload.STOPPED;
			this.uploadButton.toggle(uploadButtonVisible);
			this._error.empty();
		},
		_start: function () {
			this.uploader.start();
			this.uploadButton.hide();
			this._error.empty();
		},
		_filesAdded: function (u, files) {
			this.uploader.files.splice(0, this.uploader.files.length - 1);
		},
		_fileUploaded: function (u, file, data) {
			var f = this.queue[file.id];
			this.uploader.removeFile(file);
			f.el.hide();

			// Update file name
			var result = $.parseJSON(data.response);
			var fileDownload = this._element.parents('[data-ajax-update=true]').find('.u-download');
			var fileName = fileDownload.find('.u-download-name');
			fileName.text(result.name);
			fileDownload.show();
		},
		_uploadProgress: function (u, file) {
			var f = this.queue[file.id];
			f.progress.width($.format('{0}%', file.percent));
		},
		_uploadError: function (u, ex) {
			var exMessage = ex.message;
			switch (ex.code) {
				case plupload.HTTP_ERROR:
					var f = this.queue[ex.file.id];
					this.uploader.removeFile(ex.file);
					exMessage = this.options.error_http || exMessage;
					break;
				case plupload.FILE_SIZE_ERROR:
					exMessage = this.options.error_file_size || exMessage;
					break;
			}
			this._error.text(exMessage);
		}
	}

	$.fn.upload = function (options) {
		return $.utilis.pluginInit.call(this, 'data-u-upload', control, options, $.fn.upload.defaults);
	};

	$.fn.upload.defaults = {
		uploader: {
			url: null,
			browse_button: null,
			multi_selection: false,
			multiple_queues: true,
			runtimes: 'html5,silverlight,flash',
			flash_swf_url: 'plupload.flash.swf',
			silverlight_xap_url: 'plupload.silverlight.xap'
		},
		runtime_url: '/',
		error_http: null,
		error_file_size: null
	};

})(jQuery);
