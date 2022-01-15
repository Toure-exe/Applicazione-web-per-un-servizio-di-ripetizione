$(document).ready(function () {
    var app = new Vue ({
        el: '#restriction',
        data: {
            role: '',
            //account:  'guest',
            guest: false,
            student: false,
            admin: false,
            histories:[],
            link: '/progettoIUMTweb_war_exploded/MainServlet'
        },
        mounted() {
            this.sessionFunction();
            if (this.student)
                this.getHistory();
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
            getHistory:function () {
                var self = this;
                $.ajax({
                    url : "SessionServlet", // Url of backend (can be python, php, etc..)
                    type: "GET", // data type (can be get, post, put, delete)
                    data : {submit: "getHistory"}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        console.log("operazione history eseguita con successo");
                        self.histories = data;
                    }
                });
            },
            restrictionHistory:function (status) {
                var self = this;
                $.ajax({
                    url : "SessionServlet", // Url of backend (can be python, php, etc..)
                    type: "GET", // data type (can be get, post, put, delete)
                    data : {submit: "getRestrictedHistory", status: status}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        console.log("operazione history eseguita con successo");
                        self.histories = data;
                    }
                });
            }
        }
    });

    $("#restriction").on('click', '#restrictionSelected', function () {
        console.log("PROVA");
        if ($('#restrictionSelected').find(":selected").text() === "All bookings")
            app.getHistory();
        else if ($('#restrictionSelected').find(":selected").text() === "Active bookings")
            app.restrictionHistory(0);
        else if ($('#restrictionSelected').find(":selected").text() === "Confirmed bookings")
            app.restrictionHistory(1);
        else if ($('#restrictionSelected').find(":selected").text() === "Cancelled bookings")
            app.restrictionHistory(2);
    });

});