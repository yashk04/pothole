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
    var i = 0;
    var ref = firebase.database().ref('pothole');
    var options = {
      zoom: 17,
      center: {lat: 18.4575,lng:73.8508}
    }

    var map = new google.maps.Map(document.getElementById('map'), options);
    ref.on('child_added',function(snapshot) {

      var latitude = snapshot.val();

      if(latitude.statusFlag == 0) {
        //pothole work not assigned
        var a = latitude.potholeCount;
        var marker = new google.maps.Marker({
              icon: new google.maps.MarkerImage("http://maps.google.com/mapfiles/ms/icons/red-dot.png"),
              position:{lat: latitude.lat, lng: latitude.longi},
              map:map,
              url: "",
              title: ""+a
            });
      }
      else if(latitude.statusFlag == 1) {
        //pothole work is assigned but not started
        var b = latitude.potholeCount;
        var marker = new google.maps.Marker({
              icon: new google.maps.MarkerImage("http://maps.google.com/mapfiles/ms/icons/yellow-dot.png"),
              position:{lat: latitude.lat, lng: latitude.longi},
              map:map,
              url: "",
              title: ""+b
            });
      }
      else if(latitude.statusFlag == 2) {
        //pothole work in progress
        var c = latitude.potholeCount;
        var marker = new google.maps.Marker({
              icon: new google.maps.MarkerImage("http://maps.google.com/mapfiles/ms/icons/blue-dot.png"),
              position:{lat: latitude.lat, lng: latitude.longi},
              map:map,
              url: "",
              title: ""+c
            });
      }
      else if(latitude.statusFlag == 3) {
        //pothole work completed
        var d = latitude.potholeCount;
        var marker = new google.maps.Marker({
              icon: new google.maps.MarkerImage("http://maps.google.com/mapfiles/ms/icons/green-dot.png"),
              position:{lat: latitude.lat, lng: latitude.longi},
              map:map,
              url: "",
              title: ""+d
            });

        $("#menu4").append("<li>" + snapshot.key + "<button class='finishButton' id='" + snapshot.key +"'>Finish</button>" + "</li>");
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
                  firebase.database().ref('contractor/' + snapshot.val().contractorUid +"/allotedPothole").remove().then(function() {
                    alert("Removed succed");
                  })
                  .catch(function(error) {
                    alert(error.message);
                  });
                }

                window.location.reload();
            });
      }



     google.maps.event.addListener(marker, 'click', function(e) {

          // document.getElementById('lati').innerHTML = e.latLng.lat();
          // document.getElementById('long').innerHTML = e.latLng.lng();
          ref.orderByChild('lat').equalTo(e.latLng.lat()).on('child_added', function(snapshot) {
            console.log(snapshot.val());
            var value = snapshot.val();

            document.getElementById('image').src = value.potholeImageUid;
            document.getElementById('location').innerHTML = value.fullAddress;
            document.getElementById('potholes').innerHTML = value.potholeCount;
            document.getElementById('upvotes').innerHTML = value.noOfUpvotes;

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
            // snapshot.forEach(function(data) {
            //     console.log(data.key);

            // });
            firebase.database().ref('contractor').on('child_added',function(snapshot1) {
              if(value.contractorUid === snapshot1.key)
              {
                document.getElementById('contractorEmail').innerHTML = snapshot1.val().email;
              }

            var id = snapshot1.key;
            var emailId = snapshot1.val().email;
            if(value.statusFlag == 0 && i == 1) {

              if(snapshot1.val().busyFlag == 0) {
                $("#home").append("<li>" + emailId + "<button class='hireButton' id='" + id +"'>Hire</button>" + "</li>");
              }
              else if(snapshot1.val().busyFlag == 1) {
                $("#menu1").append("<li>" + emailId + "</li>");
              }
              else if(snapshot1.val().busyFlag == 2) {
                $("#menu2").append("<li>" + emailId + "<button class='removeButton' id='" + id +"'>Remove</button>" + "</li>");
              }
              if(snapshot1.val().busyFlag != 2) {
                $("#menu3").append("<li>" + emailId + "<button class='blackListButton' id='" + id +"'>Black List</button>" + "</li>");
              }
            }
            // document.querySelector('button').onclick = fun;
            $("button.hireButton").click(function() {
                firebase.database().ref('pothole/' + snapshot.key +"/").update({
                  contractorUid: this.id,
                  statusFlag: 1
                }, function(error) {
                  if (error) {
                    // The write failed...
                    alert(error);
                  } else {
                    // Data saved successfully!
                    alert("Data saved");
                  }
                });

                if(snapshot1.key === this.id) {
                  firebase.database().ref('contractor/' + snapshot1.key+"/allotedPothole/"+snapshot.key + "/").update({
                    potholeImageUid: value.potholeImageUid

                  }, function(error) {
                    if (error) {
                      // The write failed...
                      alert(error);
                    } else {
                      // Data saved successfully!
                      alert("Data saved");
                    }
                  });

                  firebase.database().ref('contractor/' + snapshot1.key+"/").update({
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

                window.location.reload();
            });

            $("button.blackListButton").click(function() {

                if(snapshot1.key === this.id) {

                  firebase.database().ref('contractor/' + snapshot1.key+"/").update({
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

                if(snapshot1.key === this.id) {

                  firebase.database().ref('contractor/' + snapshot1.key+"/").update({
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


        });



      });


  });


}
google.maps.event.addDomListener(window, 'load', initMap);
