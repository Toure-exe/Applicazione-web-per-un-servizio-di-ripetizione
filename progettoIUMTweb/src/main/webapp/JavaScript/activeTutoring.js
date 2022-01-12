$(document).ready(function () {
    var app = new Vue ({
        el: '#activeTutoring',
        data: {
            role: '',
            //account:  'guest',
            guest: true,
            student: false,
            admin: false,
            tutorings:[],
            link: '/progettoIUMTweb_war_exploded/MainServlet'
        },
        mounted() {
            this.sessionFunction();
            this.getTutorings();
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
            getTutorings:function(){
                var self = this;
                //obtain the array of tutorings
                $.ajax({
                    url : "SessionServlet", // Url of backend (can be python, php, etc..)
                    type: "GET", // data type (can be get, post, put, delete)
                    data : {submit: "getStudentTutoring"}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        self.tutorings = data;
                        console.log("query2");
                    }
                });
            },
            confirmTutoring:function (index) {
                var date = this.tutorings[index].date;
                var hour = this.tutorings[index].hour;
                var teacher = this.tutorings[index].teacher;
                var subject = this.tutorings[index].subject;
                $.ajax({
                    url : "SessionServlet", // Url of backend (can be python, php, etc..)
                    type: "GET", // data type (can be get, post, put, delete)
                    data : {submit: "confirmTutoring", date: date, hour: hour, teacher: teacher, subject: subject}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        if (data === "true") {
                            alert ("La tua prenotazione è stata confermata come avvenuta");
                            location.reload();
                        }
                        else alert ("Errore");
                    }
                });
            },
            cancelTutoring:function (index) {
                var date = this.tutorings[index].date;
                var hour = this.tutorings[index].hour;
                var teacher = this.tutorings[index].teacher;
                var subject = this.tutorings[index].subject;
                $.ajax({
                    url : "SessionServlet", // Url of backend (can be python, php, etc..)
                    type: "GET", // data type (can be get, post, put, delete)
                    data : {submit: "deleteTutoring", date: date, hour: hour, teacher: teacher, subject: subject}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        if (data === "true") {
                            alert ("La tua prenotazione è stata cancellata con successo");
                            location.reload();
                        }
                        else alert ("Errore");
                    }
                });
            }
        }
    });

    $(".list-group").on('click', '#confirmButton', function () {
        var index = $(this).parent().index();
        app.confirmTutoring(index);
    });

    $(".list-group").on('click', '#cancelButton', function () {
        var index = $(this).parent().index();
        app.cancelTutoring(index);
    });
});