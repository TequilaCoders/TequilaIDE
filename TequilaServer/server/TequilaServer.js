/**
*
* @version 1.0
* @author Alan Yoset García Cruz
* @author Miguel Alejandro Cámara Árciga
*/

var io = require("socket.io")(7000);
var mysql = require("mysql");

var roomno = 1;

var users;
var rooms = [];

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

   //clients++;
   //io.sockets.emit('broadcast',{ description: clients + ' clients connected!'});

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

  socket.on("joinRoom", function(connectedUser){
    joinRoom(connectedUser, connection);
  });

  socket.on("leaveRoom", function(connectedUser){
    leaveRoom(connectedUser, connection);
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

  socket.on("searchUser", function(criteria){
    searchUser(criteria, connection);
  });

  socket.on("saveCollaborator", function(collaboration){
    saveCollaborator(collaboration, connection);
  });  

  socket.on("deleteCollaborator", function(collaboration){
    deleteCollaborator(collaboration, connection);
  });  

  socket.on("deleteFile", function(file){
    deleteFile(file, connection);
  }); 

  socket.on("loadSharedProjects", function(user){
    loadSharedProjects(user, connection);
  }); 

  socket.on("runCompiler", function(project){ 
    runCompiler(project, connection);
  }); 

  socket.on("runProgram", function(project){
    runProgram(project,connection);
  });

  function runProgram(project, connection){
    switch(project.language) {
      case 'Java':
        runJava(project.mainClass);
        break;
      case 'C++':
        runCpp(project.mainClass);
        break;
      case 'py':
        runPython(project, connection, project.mainClass);
        break; 
    }
  }

  function runCompiler(project,connection){
    var query = connection.query("select * from archivo where Proyecto_idProyecto = ?",[project.projectID],function(error,result){
      if (error) {
        throw error;
      } else {
        var files = result; 
        for (var i = 0; i < files.length; i++) {
          createProgramFile(files[i]);
        }

        switch(project.language) {
          case 'Java':
            compileJava(project.mainClass);
            break;
          case 'C++':
            compileCpp(project.mainClass);
            break;
          case 'Python':
            break;
        }
      }
    });
  }

  function createProgramFile(file){
    var fs = require('fs');
    fs.writeFile(file.nombre+"."+file.tipo, file.contenido, function(err) {
      if (err) {
        return console.log(err);
      }
    });
  }

  function compileJava(mainClass){
    var spawn = require('child_process').spawn;
    var compile = spawn('javac', ['*'+'.java']);

    compile.stderr.on('data', function (data) {
      socket.emit("operationFinish", String(data));
    });

    compile.on('close', function (code) {
      if (code == 0) {
        socket.emit("operationFinish", "BUILD SUCCESSFUL, CODE: " + String(code));
      } else{
        socket.emit("operationFinish", "BUILD FAILED, CODE: " + String(code));
      }
      
    });
  }

  function compileCpp(mainClass){
    var spawn = require('child_process').spawn;
    var compile = spawn('g++', [mainClass+'.cpp']);
    
    compile.stdout.on('data', function (data) {
      socket.emit("operationFinish", "BUILD SUCCESSFUL");
    });

    compile.stderr.on('data', function (data) {
      socket.emit("operationFinish", String(data));
    });
  }

  function runJava(mainClass){
    var spawn = require('child_process').spawn;
    var run = spawn('java', [mainClass]);
    
    run.stdout.on('data', function (output) {
      socket.emit("operationFinish", String(output));
    });
    
    run.stderr.on('data', function (output) {
      socket.emit("operationFinish", String(output));
    });
    
    run.on('close', function (output) {
      socket.emit("operationFinish", String(output));
    }); 
  }

  function runCpp(mainClass){
    
  }

  function runPython(project,connection, mainClass){
    var query = connection.query("select * from archivo where Proyecto_idProyecto = ?",[project.projectID],function(error,result){
      if (error) {
        throw error;
      } else {
        var files = result; 
        for (var i = 0; i < files.length; i++) {
          createProgramFile(files[i]);
        }
      }
    });

    var spawn = require('child_process').spawn;
    var run = spawn('python', [mainClass+"."+"py"]);
    
    run.stdout.on('data', function (output) {
      socket.emit("operationFinish", String(output));
    });
    
    run.stderr.on('data', function (output) {
      socket.emit("operationFinish", String(output));
    });
    
    run.on('close', function (output) {
      socket.emit("operationFinish", String(output));
    }); 
  }

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

            var usuario = {

            idUsuario: resultado[0].idUsuario,
            alias: resultado[0].alias,
            biografia: resultado[0].biografia
          };

            socket.emit("approved", true, usuario);
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
        console.log("Usuario registrado, se emitio respuesta");
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
        socket.emit("fileSaved", result.insertId, file.name);
      }
    });
  }

  function updateFile(file, connection){
    var query = connection.query("update archivo set contenido = ? where idarchivo = ?",[file.content, file.fileID],function(error,result){
      if (error) {
        throw error;
      } else {
        io.sockets.in(16).emit("fileUpdated", file.content, "archivo actualizado exitosamente");
      }
    });
  }

  function recoverFiles(project, connection){
    //var room = project.projectID;
   //socket.join(room);
   //io.sockets.in(room).emit('connectToRoom', "You are in room no. "+room);

    var query = connection.query("select * from archivo where Proyecto_idProyecto = ?",[project.projectID],function(error,result){

      if (error) {
        throw error;
      } else {
        socket.emit("filesRecovered", true, result);
      }
    });
  }

  function joinRoom(connectedUser, connection){

    var room = connectedUser.projectID;
    var filtredRooms = [];

      console.log("se unio al nuevo room que creo");
        socket.join(room);
        users = {
          room: room,
          idUsuario: connectedUser.userID
        }
        rooms.push(users);

        for (var i = rooms.length - 1; i >= 0; i--) {
          if (rooms[i].room == room) {
            filtredRooms.push(rooms[i]);
          }
        }
        console.log("se unio al nuevo room que creo" + JSON.stringify(rooms));
        io.sockets.in(room).emit('connectToRoom', filtredRooms);  

  }

  function leaveRoom(connectedUser, connection){

    var room = connectedUser.projectID;
    var filteredRooms = [];

      console.log("dejo el room");

      console.log("rooms antes " + JSON.stringify(rooms));

        socket.leave(room);

        users = {
          room: room,
          idUsuario: connectedUser.userID
        }

        //var index = rooms.indexOf(users);
        var index = rooms.findIndex(x => x.room==room & x.idUsuario == connectedUser.userID);

        console.log("indice del usuario a eliminar " + index);

        rooms.splice(index, 1);

        console.log("rooms despues " + JSON.stringify(rooms));

        for (var i = rooms.length - 1; i >= 0; i--) {
          if (rooms[i].room == room) {
            filteredRooms.push(rooms[i]);
          }
        }

        console.log("rooms filtrados " + JSON.stringify(filteredRooms));
        io.sockets.in(room).emit('disconnectFromRoom', filteredRooms);  

        console.log("room = " + room);

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

  function searchUser(criteria, connection){
    console.log([criteria.searchCriteria]);
    var query = connection.query("select idUsuario, alias, biografia from Usuario where alias= ?",[criteria.searchCriteria],function(error,result){

      if (error) {
        throw error;
      } else {
        if (result.length > 0) {    
          if(result[0].alias == criteria.searchCriteria){
          socket.emit("searchFinalized",true, result);
          } else {
            socket.emit("searchFinalized", false, "no coincidences");
          }
          }else {
          console.log("no entra");
          socket.emit("searchFinalized", false, "no existe el usuario");
        }
        }
    });
  }

  function saveCollaborator(collaboration, connection){
    console.log([collaboration.collaboratorID]);
    var values = [collaboration.collaboratorID, collaboration.projectID];
    var query = connection.query("insert into colaborador(Usuario_idusuario, proyecto_idproyecto) values (?)",[values],function(error,result){

      if (error) {
        throw error;
      } else {
        socket.emit("collaborationSaved", true);
      }
    });
  }

  function deleteCollaborator(collaboration, connection){
    console.log([collaboration.collaboratorID]);
    var values = [collaboration.collaboratorID, collaboration.projectID];
    var query = connection.query("delete from colaborador where Usuario_idusuario=? and proyecto_idproyecto=?",values,function(error,result){

      if (error) {
        throw error;
      } else {
        socket.emit("collaborationDeleted", true);
      }
    });
  }

  function deleteFile(file, connection){
    console.log([file.fileID]);

    var query = connection.query("delete from archivo where idArchivo =?",[file.fileID],function(error,result){

      if (error) {
        throw error;
      } else {
        socket.emit("fileDeleted", true);
      }
    });
  }

  function loadSharedProjects(user, connection){
    console.log([user.userID]);

    var query = connection.query("select idProyecto, p.nombre, p.lenguaje from proyecto p inner join colaborador c on idProyecto=proyecto_idproyecto where c.usuario_idusuario = ?",[user.userID],function(error,result){

      if (error) {
        throw error;
      } else {
        console.log(result);
        socket.emit("sharedProjectsRecovered", true, result);
      }
    });
  }

});