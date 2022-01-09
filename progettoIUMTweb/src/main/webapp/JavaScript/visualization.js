$(document).ready(function () {
    var app = new Vue ({
        el: '#subjectsVisualization',
        data: {
            role: '',
            subjects:[],
            //account:  'guest',
            guest: true,
            student: false,
            admin: false,
            link: '/progettoIUMTweb_war_exploded/MainServlet'
        },
        mounted() {
            this.sessionFunction();
            this.subjectAvailable();
            console.log("entra nel mounted");
        },
        methods:{
            sessionFunction:function(){
                var self = this;
                $.ajax({
                    url : "SessionServlet", // Url of backend (can be python, php, etc..)
                    type: "POST", // data type (can be get, post, put, delete)
                    data : {submit: "getRoleSession"}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        console.log(data + "tipo: " + typeof data);
                        self.role = data;
                        switch (data) {
                            case '0': //guest
                                self.guest = true;
                                self.student = false;
                                self.admin = false;
                                break;
                            case '1': //student
                                self.guest = false;
                                self.student = true;
                                self.admin = false;
                                break;
                            case '2': //admin
                                self.guest = false;
                                self.student = false;
                                self.admin = true;
                                break;
                        }
                    }
                });
            },
            subjectAvailable:function() {
                var self = this;
                $.ajax({
                    url : "MainServlet", // Url of backend (can be python, php, etc..)
                    type: "GET", // data type (can be get, post, put, delete)
                    data : {submit: "subjectAvailable"}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        console.log(data);
                        self.subjects = data;
                    }
                });
            }
        }

    });
    $("#subjectSelected").change(function(){
        var app1 = new Vue ({
            el: '#teachersVisualization',
            data: {
                selected: '',
                teachers:[],
                link: '/progettoIUMTweb_war_exploded/MainServlet'
            },
            mounted() {
                this.getTeachers();
                console.log("entra nel mounted");
            },
            methods:{
                getTeachers:function() {
                    selected = $('#subjectSelected').find(":selected").text();
                    console.log(selected);
                    var self = this;
                    $.ajax({
                        url : "MainServlet", // Url of backend (can be python, php, etc..)
                        type: "GET", // data type (can be get, post, put, delete)
                        data : {submit: "getTeachers", subject: selected}, // data in json format
                        async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                        success: function(data) {
                            console.log(data);
                            self.teachers = data;
                        }
                    });
                }
            }
        });
    });
});


