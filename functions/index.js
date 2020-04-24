const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
const root = admin.database().ref();
const db = admin.firestore()


exports.mensajeChat = functions.database.ref('/Chat/{msgID}').onWrite( async (change, context)  => {
           var t = 'Nuevo mensaje de '+ change.after.val().senderName + '!'
           var b = change.after.val().message
           console.log(t);
           console.log(b);
           var payload = {
               notification: {
                  title: t,
                  body: b
               }
             };
             const alltokens = await admin.firestore().collection('Tokens').get();
             const tokens = [];
             console.log(tokens)
             alltokens.forEach((tokenDoc) => {
             tokens.push(tokenDoc.data().tok);
             });
             if(tokens.length > 0){
                await admin.messaging().sendToDevice(tokens, payload);
                console.log(tokens)
             }
             return true;

});

exports.crearEvento = functions.firestore
    .document('Calendario/{eventID}')
    .onWrite((change, context) => {
      const document = change.after.exists ? change.after.data() : null;
      if(document !== null){
          var titulo = document.data
          var fecha = new Date(parseInt(document.timeInMillis));
          var sfecha = fecha.toLocaleDateString('default');
          console.log(sfecha)
          var userRef = db.collection('Tokens').doc(document.idpaciente);
          return userRef
              .get()
              .then(doc => {
                if (!doc.exists) {
                  throw new Error('No such User document!');
                } else {
                  var payload = {
                       notification: {
                          title: "Nuevo evento!",
                          body: "Se le ha asignado un nuevo evento para el día " +sfecha+" titulado: "+ titulo+"."
                       }
                     };
                  admin.messaging().sendToDevice(doc.data().tok, payload)
                  return true
                }
              })
              .catch(err => {
                console.log(err)
                return false;
              });
          }
          return false;
    });

exports.cancelarEvento = functions.firestore
    .document('Calendario/{eventID}')
    .onDelete((change, context) => {
      const document = change.exists ? change.data() : null;
      if(document !== null){
          var titulo = document.data
          var fecha = new Date(parseInt(document.timeInMillis));
          var sfecha = fecha.toLocaleDateString('default');
          console.log(sfecha)
          var userRef = db.collection('Tokens').doc(document.idpaciente);
          return userRef
              .get()
              .then(doc => {
                if (!doc.exists) {
                  throw new Error('No such User document!');
                } else {
                  var payload = {
                       notification: {
                          title: "Evento anulado!",
                          body: "Se ha cancelado el evento del día " +sfecha+" titulado: "+ titulo+"."
                       }
                     };
                  admin.messaging().sendToDevice(doc.data().tok, payload)
                  return true
                }
              })
              .catch(err => {
                console.log(err)
                return false;
              });
          }
          return false;
    });