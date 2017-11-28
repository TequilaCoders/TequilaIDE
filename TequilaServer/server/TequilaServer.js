/**
*
* @version 1.0
* @author Alan Yoset García Cruz
*/

var io = require("socket.io")(7000);

var mysql = require("mysql");

var connection = mysql.createConnection({
 	host: "127.0.0.1",
 	user: "tequiladmin",
 	password: "tequiladmin",
 	database: "tequilaIDE",
 	port: 3306
});

console.log("Corriendo en el puerto 7000");

io.on("connection",function(socket) {
  console.log("conectado");

  socket.on("access", function(user){
    logIn(user, connection);
  });

  function logIn(user, connection) {
    console.log([user.alias]);
    var query = connection.query("select alias, clave from usuario where alias=?",[user.alias],function(error,result){
      if (error) {
        throw error;
      } else {
        var resultado = result;
        if (resultado.length > 0) {
          console.log("entra");
          if (resultado[0].alias == user.alias && resultado[0].clave == user.clave) {
            socket.emit("approved", true, resultado[0].idUsuario);
            console.log("Ingreso al sistema exitoso");
          } else {
            socket.emit("approved", false, "clave incorrecta");
            console.log("Ingreso al sistema fallido");
          }
        } else {
          console.log("no entra");
          socket.emit("approved", false, "no existe el usuario");
        }
      }
    });
  }
});
