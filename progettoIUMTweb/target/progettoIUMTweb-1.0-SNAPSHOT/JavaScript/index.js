$(document).ready(function () {
    var app = new Vue ({
        el: '#sessionID',
        data: {
            session: '',
            //account:  'guest',
            guest: true,
            student: false,
            admin: false,
            link: '/progettoIUMTweb_war_exploded/MainServlet'
        },
        mounted() {
            this.sessionFunction();
            console.log("entra nel mounted");
        },
        methods:{
            sessionFunction:function(){
                var self = this;
                $.ajax({
                    url : "MainServlet", // Url of backend (can be python, php, etc..)
                    type: "POST", // data type (can be get, post, put, delete)
                    data : {submit: "indexSession"}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        self.session = data;
                    }
                });
                var self1 = this;
                $.ajax({
                    url : "MainServlet", // Url of backend (can be python, php, etc..)
                    type: "POST", // data type (can be get, post, put, delete)
                    data : {submit: "getRoleSession"}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {

                        console.log(data + "tipo: " + typeof data);
                        switch (data) {
                            case '0':
                                self1.guest = true;
                                self1.student = false;
                                self1.admin = false;
                                break;
                            case '1':
                                self1.guest = false;
                                self1.student = true;
                                self1.admin = false;
                                break;
                            case '2':
                                self1.guest = false;
                                self1.student = false;
                                self1.admin = true;
                                break;
                        }
                    }
                });
            }
        }
    });
});