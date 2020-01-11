
      
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

     var locations0=[],locations1=[],locations2=[],locations3=[],locations4=[];
     //var count0=1,count1=1,count2=1,count3=1;
     
     var ref = firebase.database().ref('pothole');
     
     ref.once('value').then(function(snapshot) {
               snapshot.forEach(function(childSnapshot){
     //alert("fffff");
                      
                 if(childSnapshot.val().statusFlag==0)
                 {
                      locations0.push(childSnapshot.key,childSnapshot.val().fullAddress); 
                      // count0++;    
                      // alert("count0"+count0);
                       alert(locations0);       
                 }
                 else if(childSnapshot.val().statusFlag==1)
                 {
                     locations1.push(childSnapshot.key,childSnapshot.val().fullAddress);
                    //  count1++;
                    //  alert("count1"+count1);
                      alert(locations1); 
                 }
                 else if(childSnapshot.val().statusFlag==2)
                 {
                     locations2.push(childSnapshot.key,childSnapshot.val().fullAddress);
                    //  count2++;
                    //  alert("count2"+count1);
                    //  alert(locations2); 
                 }
                 else if(childSnapshot.val().statusFlag==3)
                 {
                     locations3.push(childSnapshot.key,childSnapshot.val().fullAddress);
                    // count3++;
                    // alert("count3"+count3);
                    //  alert(locations3); 
                 } 
                 else if(childSnapshot.val().statusFlag==4)
                 {
                     locations4.push(childSnapshot.key,childSnapshot.val().fullAddress);
                    // count3++;
                    // alert("count3"+count3);
                    //  alert(locations3); 
                 }
     
                 
               });
               google.charts.load("current", {packages:["corechart"]});
               google.charts.setOnLoadCallback(drawChart);

     });
     
    //  google.charts.load("current", {packages:["corechart"]});
    //  google.charts.setOnLoadCallback(drawChart);
     
        function drawChart() {
          
          // alert("count0"+locations0.length);
          // alert("count1"+locations1.length);
          // alert("count2"+locations2.length);
          // alert("count3"+locations3.length);


          var data = google.visualization.arrayToDataTable([
            ['Work', 'Potholes'],
            ['WORK IN PROGRESS',  ( locations2.length/2)],
            ['WORK FINISHED BUT NOT VERIFIED',     (locations3.length/2)],
            ['WORK NOT YET ADDRESSED', ( locations0.length/2)],
            ['WORK ASSIGNED', (locations1.length/2)],
            ['WORK DONE SUCCESSFULLY', (locations4.length/2)]
          ]);
     
          var options = {
            title: 'STATUS REPORT',
            is3D: true,
        
          };

           
          var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));
     
     function selectHandler() {
       var selectedItem = chart.getSelection()[0];
       if (selectedItem) {
         var topping = data.getValue(selectedItem.row, 0);
         //alert('The user selected ' + topping);
          var i;
         if(topping=='WORK IN PROGRESS')
         {
          //alert("count2"+locations2.length);
             $("#table_body").empty();
             for(i=1;i<locations2.length;i+2)
             $("#table_body").append("<tr><td>  " +locations2[i] +"</td><td> <input type='button' onclick=fun(this.id) id=" +locations2[i-1] +" value='VIEW DETAILS'> </input> </td></tr>");
         }
         else if(topping=='WORK FINISHED BUT NOT VERIFIED')
         {
          //alert("count3"+locations3.length);
             $("#table_body").empty();
             for(i=1;i<locations3.length;i=i+2)
             $("#table_body").append("<tr><td> " +locations3[i] +"</td><td> <input type='button' onclick=fun(this.id) id=" +locations3[i-1] +" value='VIEW DETAILS'> </input></td></tr>");
            
         }
         else if(topping=='WORK NOT YET ADDRESSED')
         {
          //alert("count0"+locations0.length);
             $("#table_body").empty();
             for(i=1;i<locations0.length;i=i+2)
             $("#table_body").append("<tr><td> " +locations0[i]+"</td><td> <input type='button' onclick=fun(this.id) id=" +locations0[i-1] +" value='VIEW DETAILS'> </input></td></tr>");
            
         }
         else if(topping=='WORK ASSIGNED')
         {
          //alert("count1"+locations1.length);
             $("#table_body").empty();
             for(i=1;i<locations1.length;i=i+2)
             $("#table_body").append("<tr><td> " +locations1[i]+ "</td><td> <input type='button' onclick=fun(this.id) id=" +locations1[i-1] +" value='VIEW DETAILS'></input></td></tr>");
            
         }
         else if(topping=='WORK DONE SUCCESSFULLY')
         {
          //alert("count1"+locations1.length);
             $("#table_body").empty();
             for(i=1;i<locations4.length;i=i+2)
             $("#table_body").append("<tr><td> " +locations4[i]+ "</td><td> <input type='button' onclick=fun(this.id) id=" +locations4[i-1] +" value='VIEW DETAILS'></input></td></tr>");
            
         }
       }
     }
             
             google.visualization.events.addListener(chart, 'select', selectHandler);    
             chart.draw(data, options);
    }



    function fun(id)
    {
      alert(id);

      var a=firebase.database().ref('pothole/'+id);
      
      a.once('value').then(function(snapshot) {
         alert(snapshot);
        console.log(snapshot.val());
        var value = snapshot.val();
        
        document.getElementById('image').src = value.potholeImageUid;
        document.getElementById('location').innerHTML = value.fullAddress;
        document.getElementById('potholes').innerHTML = value.potholeCount;
        document.getElementById('upvotes').innerHTML = value.noOfUpvotes;
        document.getElementById('contractorEmail').innerHTML = value.contractorEmail;

        if(value.statusFlag == 0)
        {
          document.getElementById('work').innerHTML = "Work not started";
        }
        else if(value.statusFlag == 1)
        {
          document.getElementById('work').innerHTML = "Work is assigned";

        }
        else if(value.statusFlag == 2)
        {
          document.getElementById('work').innerHTML = "Work is in progress";
        }
        else if(value.statusFlag == 3)
        {
          document.getElementById('work').innerHTML = "Work finished but not verified";
        }
        else if(value.statusFlag == 4)
        {
          document.getElementById('work').innerHTML = "Work successfully done";
        }
        // snapshot.forEach(function(data) {
        //     console.log(data.key);

        // });
       
  

    });

   

    }

     firebase.database().ref('contractor').on('child_added',function(snapshot1) {
              // if(value.contractorUid === snapshot1.key)
              // {
              //   document.getElementById('contractorEmail').innerHTML = snapshot1.val().email;
              // }

            var id = snapshot1.key;
            var emailId = snapshot1.val().email;
            // if(value.statusFlag == 0) {

              if(snapshot1.val().busyFlag == 0) {
                $("#home").append("<div class='row'><div class='col'><ul><li>" + emailId + "</li></ul></div>" + "<div class='col'><button class='hireButton btn btn-md btn-success float-right' id='" + id +"'>Hire</button></div></div><br>");
              }
              else if(snapshot1.val().busyFlag == 1) {
                $("#menu1").append("<div class='row'><div class='col'><ul><li>" + emailId + "</li></ul></div>" + "<br>");
              }
              else if(snapshot1.val().busyFlag == 2) {
                $("#menu2").append("<div class='row'><div class='col'><ul><li>" + emailId + "</li></ul></div>" + "<div class='col'><button class='removeButton btn btn-md btn-danger float-right' id='" + id +"'>Remove</button></div></div><br>");
              }
              if(snapshot1.val().busyFlag != 2) {
                $("#menu3").append("<div class='row'><div class='col'><ul><li>" + emailId + "</li></ul></div>" + "<div class='col'><button class='blackListButton btn btn-md btn-danger float-right' id='" + id +"'>Add to Blacklist</button></div></div><br>");
              }

              $("button.hireButton").click(function() {
                var i;
                // for(i=0;i < locations.length;i++) {
                  var email;
                  if(id === this.id) {
                    email = snapshot1.val().email;
                  }
                  // alert(email);
                  firebase.database().ref('pothole/' + locations[i] +"/").update({
                    contractorUid: this.id,
                    statusFlag: 1,
                    contractorEmail: email
                  }, function(error) {
                    if (error) {
                      // The write failed...
                      alert(error.message);
                    } else {
                      // Data saved successfully!
                      alert("Data saved");
                    }
                  });

                  if(id === this.id) {
                    firebase.database().ref('contractor/' + id +"/allotedPothole/"+ locations[i] + "/").update({
                      potholeImageUid: potholeImages[i]

                    }, function(error) {
                      if (error) {
                        // The write failed...
                        alert(error.message);
                      } else {
                        // Data saved successfully!
                        alert("Data saved");
                      }
                    });

                    firebase.database().ref('contractor/' + id +"/").update({
                      busyFlag: 1

                    }, function(error) {
                      if (error) {
                        // The write failed...
                        alert(error.message);
                      } else {
                        // Data saved successfully!
                        alert("Data saved");
                      }
                    });
                  }


                // }
                window.location.reload();
              });


              $("button.blackListButton").click(function() {

                  if(id === this.id) {

                    firebase.database().ref('contractor/' + id +"/").update({
                      busyFlag: 2

                    }, function(error) {
                      if (error) {
                        // The write failed...
                        alert(error);
                      } else {
                        // Data saved successfully!
                        alert("Data saved");
                      }
                    });
                  }

                  window.location.reload();
              });

              $("button.removeButton").click(function() {

                  if(id === this.id) {

                    firebase.database().ref('contractor/' + id +"/").update({
                      busyFlag: 0

                    }, function(error) {
                      if (error) {
                        // The write failed...
                        alert(error);
                      } else {
                        // Data saved successfully!
                        alert("Data saved");
                      }
                    });
                  }

                  window.location.reload();
              });

      });
