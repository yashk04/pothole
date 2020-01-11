var firebaseConfig = {
           apiKey: "AIzaSyBKSRuQIWn9SsOyfMkaLi5xJVo2UU__ijw",
           authDomain: "pothole-b8122.firebaseapp.com",
           databaseURL: "https://pothole-b8122.firebaseio.com",
           projectId: "pothole-b8122",
           storageBucket: "pothole-b8122.appspot.com",
           messagingSenderId: "543188174392",
           appId: "1:543188174392:web:a383b8098730b3f519c8cd",
           measurementId: "G-7822SGDGKJ"
         };
         // Initialize Firebase
         firebase.initializeApp(firebaseConfig);
         //firebase.analytics();



var ref = firebase.database().ref('user');
   var arr=[];
   var j=0;


function abc(){
          //alert(arr);
          var i;
     for(i=5;i>0;i=i-2)
    {
        //alert(arr);
    $("#table_body").append("<tr><td><p>" + arr[i-1]+"</td><td> " + arr[i]+"</p></td></tr>");
    }
     }

   ref.orderByChild('points').limitToLast(3).on('child_added', function(snapshot){
       //alert("100");
       var value=snapshot.val().email;
       var points=snapshot.val().points;
       arr.push(value,points);
     //alert(arr);
     j++;
     //alert(j);
     if(j==3)
        {//alert("in");
        abc();}
   });
