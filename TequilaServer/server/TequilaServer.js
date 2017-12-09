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

  socket.on("aliasChanged", function(alias){
    verifyAlias(alias, connection);
  });

  socket.on("emailChanged", function(email){
    verifyEmail(email, connection);
  });

  socket.on("saveUser", function(user){
    registerUser(user, connection);
  });

  socket.on("saveFile", function(file){
    saveFile(file, connection);
  });

  socket.on("updateFile", function(file){
    updateFile(file, connection);
  });

  socket.on("loadFiles", function(project){
    recoverFiles(project, connection);
  });  

  socket.on("loadProjects", function(user){
    recoverProjects(user, connection);
  });  

  socket.on("saveProject", function(project){
    saveProject(project, connection);
  }); 

  socket.on("getCollaborators", function(projectID){
    getCollaborators(projectID, connection);
  });  

  function logIn(user, connection) {
    console.log([user.alias]);
    var query = connection.query("select * from usuario where alias=?",[user.alias],function(error,result){
      if (error) {
        throw error;
      } else {
        var resultado = result;
        if (resultado.length > 0) {
          console.log("entra");
          if (resultado[0].alias == user.alias && resultado[0].clave == user.clave) {
            socket.emit("approved", true, resultado);
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

  function verifyAlias(alias, connection) {
    console.log([alias.alias]);
    var query = connection.query("select alias from usuario where alias=?",[alias.alias],function(error,result){
      if (error) {
        throw error;
      } else {
        var resultado = result;
        if (resultado.length > 0) {
          console.log("entra");
          if (resultado[0].alias == alias.alias) {
            socket.emit("aliasDuplicated", true, "alias duplicado");
            console.log("Alias duplicado");
          } else {
            socket.emit("aliasDuplicated", false, "alias no duplicado");
            console.log("alias no duplicado");
          }
        } else {
          console.log("no entra");
          socket.emit("aliasDuplicated", false, "alias no duplicado");
        }
      }
    });
  }

  function verifyEmail(email, connection) {
    console.log([email.email]);
    var query = connection.query("select correo from usuario where correo=?",[email.email],function(error,result){
      if (error) {
        throw error;
      } else {
        var resultado = result;
        if (resultado.length > 0) {
          console.log("entra");
          if (resultado[0].correo == email.email) {
            socket.emit("emailDuplicated", true, resultado[0].correo);
            console.log("email duplicado");
          } else {
            socket.emit("emailDuplicated", false, "email no duplicado");
            console.log("email no duplicado");
          }
        } else {
          console.log("no entra");
          socket.emit("emailDuplicated", false, "email no duplicado");
        }
      }
    });
  }

  function registerUser(user, connection){
    console.log([user.name]);
    var values = [user.name, user.alias, user.email, user.password];
    var query = connection.query("insert into usuario(nombres, alias, correo, clave) values (?)",[values],function(error,result){

      if (error) {
        throw error;
      } else {
        socket.emit("registrationSuccesful", true, "usuario registrado");
      }
    });
  }

  function saveFile(file, connection){
    console.log([file.name]);
    var values = [file.name, file.content, file.projectID, file.fileType];
    var query = connection.query("insert into archivo(nombre, contenido, Proyecto_idProyecto, tipo) values (?)",[values],function(error,result){

      if (error) {
        throw error;
      } else {
        socket.emit("fileSaved", true, "archivo guardado exitosamente");
      }
    });
  }

  function updateFile(file, connection){
    console.log([file.name]);
    var values = [file.name, file.content, file.fileID];
    var query = connection.query("update archivo set nombre = ?, contenido = ? where idarchivo = ?",[file.name, file.content, file.fileID],function(error,result){

      if (error) {
        throw error;
      } else {
        socket.emit("fileUpdated", true, "archivo actualizado exitosamente");
      }
    });
  }

  function recoverFiles(project, connection){
    console.log([project.projectID]);
    var query = connection.query("select * from archivo where Proyecto_idProyecto = ?",[project.projectID],function(error,result){

      if (error) {
        throw error;
      } else {
        socket.emit("filesRecovered", true, result);
      }
    });
  }

  function recoverProjects(user, connection){
    console.log([user.userID]);
    var query = connection.query("select * from proyecto where Usuario_idUsuario = ?",[user.userID],function(error,result){

      if (error) {
        throw error;
      } else {
        socket.emit("projectsRecovered", true, result);
      }
    });
  }

  function saveProject(project, connection){
    console.log([project.name]);
    var values = [project.name, project.programmingLanguage, project.userID];
    var query = connection.query("insert into proyecto(nombre, lenguaje, Usuario_idUsuario) values (?)",[values],function(error,result){

      if (error) {
        throw error;
      } else {
        socket.emit("projectSaved", true, result.insertId);
      }
    });
  }

  function getCollaborators(projectID, connection){
    console.log([projectID.projectID]);
    var query = connection.query("select idUsuario, alias, biografia from Usuario inner join Colaborador on idusuario=usuario_idusuario where proyecto_idProyecto= ?",[projectID.projectID],function(error,result){

      if (error) {
        throw error;
      } else {
        socket.emit("collaboratorsRecovered", result);
      }
    });
  }

});