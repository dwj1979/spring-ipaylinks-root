var global_model = 'add';
var FormValidation = function () {
    var valideProperty,valideProject;
    // validation using icons
    var handleValidation2 = function() {
        // for more info visit the official plugin documentation: 
            // http://docs.jquery.com/Plugins/Validation

            var form2 = $('#projectForm');
            var error2 = $('.alert-danger', form2);
            var success2 = $('.alert-success', form2);

            valideProject  = form2.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: "",  // validate all fields including form hidden input
                rules: {
                    name: {
                        minlength: 2,
                        required: true
                    },
                    springApplicationName: {
                        minlength: 2,
                        required: true
                    },
                    desc: {
                        minlength: 2,
                        required: true
                    },

                },

                invalidHandler: function (event, validator) { //display error alert on form submit              
                    success2.hide();
                    error2.show();
                    App.scrollTo(error2, -200);
                },

                errorPlacement: function (error, element) { // render error placement for each input type
                    var icon = $(element).parent('.input-icon').children('i');
                    icon.removeClass('fa-check').addClass("fa-warning");  
                    icon.attr("data-original-title", error.text()).tooltip({'container': 'body'});
                },

                highlight: function (element) { // hightlight error inputs
                    $(element)
                        .closest('.form-group').removeClass("has-success").addClass('has-error'); // set error class to the control group   
                },

                unhighlight: function (element) { // revert the change done by hightlight
                    
                },

                success: function (label, element) {
                    var icon = $(element).parent('.input-icon').children('i');
                    $(element).closest('.form-group').removeClass('has-error').addClass('has-success'); // set success class to the control group
                    icon.removeClass("fa-warning").addClass("fa-check");
                },

                submitHandler: function (form) {
                    success2.show();
                    error2.hide();
                    var data = $("#projectForm").serialize();
                    data = data + "&path=" + $('#path').val();
                    $.ajax({
                        url: $("#projectForm").attr('action'),
                        type: 'POST',
                        async: false,
                        data: data,
                        dataType: 'json',
                        success: function(data){
                            if(!data.success){
                                alert("增加项目失败");
                            }else{
                                $('#closeProject').click();
                                location.href = $('#home').attr('href') + "?appName=" + $("#projectForm").find('[name="springApplicationName"]').val();
                            }
                        }
                    });
                }
            });
    }

    var handleValidation3 = function() {
        // for more info visit the official plugin documentation:
        // http://docs.jquery.com/Plugins/Validation

        var form2 = $('#propertyForm');
        var error2 = $('.alert-danger', form2);
        var success2 = $('.alert-success', form2);

        valideProperty = form2.validate({
            errorElement: 'span', //default input error message container
            errorClass: 'help-block help-block-error', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            ignore: "",  // validate all fields including form hidden input
            rules: {
                name: {
                    minlength: 2,
                    required: true
                },
                value: {
                    minlength: 1,
                    required: true
                },
                desc: {
                    minlength: 2,
                    required: true
                },

            },

            invalidHandler: function (event, validator) { //display error alert on form submit
                success2.hide();
                error2.show();
                App.scrollTo(error2, -200);
            },

            errorPlacement: function (error, element) { // render error placement for each input type
                var icon = $(element).parent('.input-icon').children('i');
                icon.removeClass('fa-check').addClass("fa-warning");
                icon.attr("data-original-title", error.text()).tooltip({'container': 'body'});
            },

            highlight: function (element) { // hightlight error inputs
                $(element)
                    .closest('.form-group').removeClass("has-success").addClass('has-error'); // set error class to the control group
            },

            unhighlight: function (element) { // revert the change done by hightlight

            },

            success: function (label, element) {
                var icon = $(element).parent('.input-icon').children('i');
                $(element).closest('.form-group').removeClass('has-error').addClass('has-success'); // set success class to the control group
                icon.removeClass("fa-warning").addClass("fa-check");
            },

            submitHandler: function (form) {
                success2.show();
                error2.hide();
                var url = $('#propertyForm').attr('action');
                if('add' != global_model){
                    url = $('#edit').attr('href');
                }
                var data = $("#propertyForm").serialize();
                data = data + "&path=" + $('#path').val();
                $.ajax({
                    url: url,
                    type: 'POST',
                    async: false,
                    data: data,
                    dataType: 'json',
                    success: function(data){
                        if(!data.success){
                            alert("更新失败");
                        }else{
                            $('#closeProperty').click();
                            refreshTable();
                        }
                    }
                });
            }
        });


    }

    var handleWysihtml5 = function() {
        if (!jQuery().wysihtml5) {
            
            return;
        }

        if ($('.wysihtml5').size() > 0) {
            $('.wysihtml5').wysihtml5({
                "stylesheets": ["../assets/global/plugins/bootstrap-wysihtml5/wysiwyg-color.css"]
            });
        }
    }

    return {
        //main function to initiate the module
        init: function () {
            handleWysihtml5();
            handleValidation2();
            handleValidation3();
        },
        reset: function (form) {
            var form2;
            if(form == 'property'){
                form2 = $('#propertyForm');
            }else{
                form2 = $('#projectForm');
            }
            form2[0].reset();
            var error2 = $('.alert-danger', form2);
            var success2 = $('.alert-success', form2);
            success2.hide();
            error2.hide();
            form2.find('i').removeClass("fa-warning").removeClass("fa-check");
            form2.find('.form-group').removeClass("has-error").removeClass("has-success");
        }
    };

}();
function reset(form,model){
    FormValidation.reset(form);
    if('property' == form){
        global_model = model;
        if('edit' ==  model){
            if(choseRow() == 0){
                alert("请选择一行");
                var evt = event || (event = window.event);
                evt.preventDefault();
                evt.stopPropagation();
            }else{
                var jsonVar = showRow();
                $("#propertyForm").find("[name='name']").val(jsonVar.name);
                $("#propertyForm").find("[name='value']").val(jsonVar.value);
                $("#propertyForm").find("[name='desc']").val(jsonVar.desc);
            }
        }
    }
}
function deleteProperty() {
    if(choseRow() == 0){
        alert("请选择一行");
    }else{
        var url = $('#deleteProperty').attr('href');
        var data = showRow();
        $.ajax({
            url: url,
            type: 'POST',
            async: false,
            data: data,
            dataType: 'json',
            success: function(data){
                if(!data.success){
                    alert("更新失败");
                }else{
                    refreshTable();
                }
            }
        });
    }
}
jQuery(document).ready(function() {
    FormValidation.init();
});