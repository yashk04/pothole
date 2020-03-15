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
firebase.analytics();

function initMap()
{
    var marker;
    var locations = [];
    var potholeImages = [];
    var ref = firebase.database().ref('pothole');
    var options = {
      zoom: 12,
      center: {lat: 18.5204,lng:73.8567}
    }

    var map = new google.maps.Map(document.getElementById('map'), options);
    ref.on('child_added',function(snapshot) {

      var latitude = snapshot.val();

      if(latitude.statusFlag == 0) {
        //pothole work not assigned
        var a = latitude.potholeCount;
        marker = new google.maps.Marker({
              icon: new google.maps.MarkerImage("http://maps.google.com/mapfiles/ms/icons/red-dot.png"),
              position:{lat: latitude.lat, lng: latitude.longi},
              map:map,
              title: "Pothole Count:"+a
            });
      }
      else if(latitude.statusFlag == 1) {
        //pothole work is assigned but not started
        var b = latitude.potholeCount;
        marker = new google.maps.Marker({
              icon: new google.maps.MarkerImage("http://maps.google.com/mapfiles/ms/icons/yellow-dot.png"),
              position:{lat: latitude.lat, lng: latitude.longi},
              map:map,
              title: "Pothole Count:"+b
            });
      }
      else if(latitude.statusFlag == 2) {
        //pothole work in progress
        var c = latitude.potholeCount;
        marker = new google.maps.Marker({
              icon: new google.maps.MarkerImage("http://maps.google.com/mapfiles/ms/icons/blue-dot.png"),
              position:{lat: latitude.lat, lng: latitude.longi},
              map:map,
              title: "Pothole Count:"+c
            });
      }
      else if(latitude.statusFlag == 3) {
        //pothole work completed
        var d = latitude.potholeCount;

        marker = new google.maps.Marker({
              icon: new google.maps.MarkerImage("http://maps.google.com/mapfiles/ms/icons/green-dot.png"),
              position:{lat: latitude.lat, lng: latitude.longi},
              map:map,
              title: "Pothole Count:"+d
            });


        $("#menu4").append("<div class='row'><div class='col'><ul><li>" + snapshot.key + "</li></ul></div>" + "<div class='col'><button class='finishButton btn btn-md btn-success float-right' id='" + snapshot.key +"'>Finish</button></div></div><br>");
        $("button.finishButton").click(function() {
                firebase.database().ref('pothole/' + snapshot.key +"/").update({
                  statusFlag: 4,
                  noOfUpvotes: 0
                }, function(error) {
                  if (error) {
                    // The write failed...
                    alert(error);
                  } else {
                    // Data saved successfully!
                    alert("Data saved");
                  }
                });

                if(snapshot.key === this.id) {
                  // alert("yes");
                  firebase.database().ref('contractor/' + snapshot.val().contractorUid +"/").update({
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
                  firebase.database().ref('contractor/' + snapshot.val().contractorUid +"/allotedPothole/" + snapshot.key).remove().then(function() {
                    alert("Removed succeed");
                  })
                  .catch(function(error) {
                    alert(error.message);
                  });
                }

                window.location.reload();
            });
      }


//click started
google.maps.event.addListener(marker, 'click', function(e) {

     // document.getElementById('lati').innerHTML = e.latLng.lat();
     // document.getElementById('long').innerHTML = e.latLng.lng();
     // var flag = 0;
     ref.orderByChild('lat').equalTo(e.latLng.lat()).on('child_added', function(snapshot) {
       var value = snapshot.val();
       if(!locations.includes(value.potholeUid) && value.statusFlag == 0) {
         locations.push(value.potholeUid);
         potholeImages.push(value.potholeImageUid);
         alert(locations.length);
       }else if(locations.includes(value.potholeUid)){
         locations.splice(locations.indexOf(value.potholeUid), 1);
         potholeImages.splice(potholeImages.indexOf(value.potholeImageUid), 1);
         alert(locations.length);
       }

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
         document.getElementById('work').innerHTML = "Work is finished";
       }
     });

   });

///click end
});//ref for pothole marking ends here


//displaying emails starts here
            var email;
            var i;
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


                if(id === this.id) {
                  email = snapshot1.val().email;
                }
                for(i=0;i < locations.length;i++) {


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
                        alert(error);
                      } else {
                        // Data saved successfully!
                        alert("Data saved");
                      }
                    });

                  }


                }
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
}
google.maps.event.addDomListener(window, 'load', initMap);
