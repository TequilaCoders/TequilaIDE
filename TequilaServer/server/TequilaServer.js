/**
 *
 * @version 1.0
 * @author Alan Yoset García Cruz
 * @author Miguel Alejandro Cámara Árciga
 */

var io = require("socket.io")(7000);
var mysql = require("mysql");

var room;

var users;
var projectsRooms = [];
var filesRooms = [];
var user;

var connection = mysql.createConnection({
  host: "127.0.0.1",
  user: "tequiladmin",
  password: "tequiladmin",
  database: "tequilaIDE",
  port: 3306
});

console.log("Corriendo en el puerto 7000");

io.on("connection", function(socket) {
  console.log("conectado");

  socket.on("disconnect", (reason) => {
    console.log("desconectado");
    filesRooms = [];
    if (user != null) {
      leaveProjectRoom(user, connection);
    }
  });

  //clients++;
  //io.sockets.emit('broadcast',{ description: clients + ' clients connected!'});

  socket.on("access", function(user) {
    logIn(user, connection);
  });

  socket.on("aliasChanged", function(alias) {
    verifyAlias(alias, connection);
  });

  socket.on("emailChanged", function(email) {
    verifyEmail(email, connection);
  });

  socket.on("saveUser", function(user) {
    registerUser(user, connection);
  });

  socket.on("saveFile", function(file) {
    saveFile(file, connection);
  });

  socket.on("updateFile", function(file) {
    updateFile(file, connection);
  });

  socket.on("loadFiles", function(project) {
    recoverFiles(project, connection);
  });

  socket.on("reloadFiles", function(project) {
    reloadFiles(project, connection);
  });

  socket.on("joinProjectRoom", function(connectedUser) {
    joinProjectRoom(connectedUser, connection);
  });

  socket.on("leaveProjectRoom", function(connectedUser) {
    leaveProjectRoom(connectedUser, connection);
  });

  socket.on("joinFilesRoom", function(file) {
    joinFilesRoom(file, connection);
  });

  socket.on("leaveFilesRoom", function() {
    leaveFilesRoom(connection);
  });

  socket.on("loadProjects", function(user) {

    recoverProjects(user, connection);
  });

  socket.on("saveProject", function(project) {
    saveProject(project, connection);
  });

  socket.on("searchProject", function(criteria) {
    searchProject(criteria, connection);
  });

  socket.on("getCollaborators", function(projectID) {
    getCollaborators(projectID, connection);
  });

  socket.on("searchUser", function(criteria) {
    searchUser(criteria, connection);
  });

  socket.on("saveCollaborator", function(collaboration) {
    saveCollaborator(collaboration, connection);
  });

  socket.on("deleteCollaborator", function(collaboration) {
    deleteCollaborator(collaboration, connection);
  });

  socket.on("deleteFile", function(file) {
    deleteFile(file, connection);
  });

  socket.on("loadSharedProjects", function(user) {
    loadSharedProjects(user, connection);
  });

  socket.on("runCompiler", function(project) {
    createProgramFiles(project, connection);
    compileProject(project.language, project.projectID);
  });

  socket.on("runProgram", function(project) {
    if (project.language == 'py') {
      createProgramFiles(project, connection);
      runProgram(project.projectID, project.mainClass, project.language, project.arguments);
    } else {
      runProgram(project.projectID, project.mainClass, project.language, project.arguments);
    }

  });

  socket.on("updateBiography", function(user) {
    updateBiography(user, connection);
  });

  /**
   * Crea un archivo
   * @param {string} fileName nombre del archivo
   * @param {string} fileType extensión del archivo
   * @param {string} fileContent contenido del archivo
   * @param {string} directory directorio donde se desea guardar el archivo
   */
  function createFile(fileName, fileType, fileContent, directory) {
    var fs = require('fs');
    var filePath = directory + "/" + fileName + "." + fileType;

    fs.writeFile(filePath, fileContent, function(err) {
      if (err) {
        return console.log(err);
      }
    });
  }

  /**
   * Crea los archivos de programa de un proyecto dado
   * @param {JSON} project JSON object de tipo proyecto
   * @param {MySQL} connection la conexión con la base de datos MySQL
   */
  function createProgramFiles(project, connection) {
    var query = connection.query("select * from archivo where Proyecto_idProyecto = ?", [project.projectID], function(error, result) {
      if (error) {
        throw error;
      } else {
        var files = result;
        var fs = require('fs');
        var directory = String(project.projectID);

        if (!fs.existsSync(directory)) {
          fs.mkdirSync(directory);
        }

        for (var i = 0; i < files.length; i++) {
          createFile(files[i].nombre, files[i].tipo, files[i].contenido, directory);
        }
      }
    });
  }

  /**
   * Compila los archivos de una extensión determinada (java o cpp) en un directorio determinado
   * @param {string} language el lenguaje de los archivos que se van a compilar
   * @param {string} directory el directorio donde se encuntran los archivos
   */
  function compileProject(language, directory) {
    var spawn = require('child_process').spawn;

    if (language == 'java') {
      var compile = spawn('javac', [directory + '/*.java']);
    } else {
      var compile = spawn('g++', ['-o', directory+'/'+directory+'Program', directory+'/*.cpp']);
    }

    compile.stderr.on('data', function(data) {
      socket.emit("compilationFinish", 1, String(data));
    });

    compile.on('close', function(code) {
      socket.emit("compilationFinish", code);
    });
  }

  function runProgram(directory, mainClass, language, args) {
    switch (language) {
      case 'java':
        var command = 'java -cp ' + directory + ' ' + mainClass + ' ' + args;
        break;
      case 'cpp':
        var command = directory + '\\' + directory + 'Program ' + args;
        break;
      case 'py':
        var command = 'python '+ directory + '/' + mainClass + '.py ' + args;
        break;
    }

    var exec = require('child_process').exec, child;
    child = exec(command, function(error, stdout, stderr) {
        socket.emit("operationFinish", stdout);
        socket.emit("operationFinish", stderr);
        if (error !== null) {
          console.log('exec error: ' + error);
        }
      });

  }

  function logIn(user, connection) {
    var query = connection.query("select * from usuario where alias=?", [user.alias], function(error, result) {
      if (error) {
        throw error;
      } else {
        var resultado = result;
        if (resultado.length > 0) {
          if (resultado[0].alias == user.alias && resultado[0].clave == user.clave) {
            var usuario = {
              idUsuario: resultado[0].idUsuario,
              alias: resultado[0].alias,
              biografia: resultado[0].biografia,
              correo: resultado[0].correo
            };
            socket.emit("approved", true, usuario);
          } else {
            socket.emit("approved", false, 1);
          }
        } else {
          socket.emit("approved", false, 2);
        }
      }
    });
  }

  function verifyAlias(alias, connection) {
    console.log([alias.alias]);
    var query = connection.query("select alias from usuario where alias=?", [alias.alias], function(error, result) {
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
    var query = connection.query("select correo from usuario where correo=?", [email.email], function(error, result) {
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

  function registerUser(user, connection) {
    console.log([user.name]);
    var values = [user.name, user.alias, user.email, user.password];
    var query = connection.query("insert into usuario(nombres, alias, correo, clave) values (?)", [values], function(error, result) {

      if (error) {
        throw error;
      } else {
        console.log("Usuario registrado, se emitio respuesta");
        socket.emit("registrationSuccesful", true, "usuario registrado");
      }
    });
  }

  function saveFile(file, connection) {
    console.log([file.name]);
    var values = [file.name, file.content, file.projectID, file.fileType];
    var query = connection.query("insert into archivo(nombre, contenido, Proyecto_idProyecto, tipo) values (?)", [values], function(error, result) {

      if (error) {
        throw error;
      } else {
        socket.emit("fileSaved", result.insertId, file.name);
      }
    });
  }

  function updateFile(file, connection) {
    var query = connection.query("update archivo set contenido = ? where idarchivo = ?", [file.content, file.fileID], function(error, result) {
      if (error) {
        throw error;
      } else {

        console.log("broadcasting al room = " + "room-" + file.fileID);
        socket.broadcast.to("room-" + file.fileID).emit("fileUpdated", file.content, file.fileID);
      }
    });
  }

  function recoverFiles(project, connection) {

    var query = connection.query("select * from archivo where Proyecto_idProyecto = ?", [project.projectID], function(error, result) {

      if (error) {
        throw error;
      } else {
        socket.emit("filesRecovered", true, result);
      }
    });
  }

  function reloadFiles(project, connection) {

    var query = connection.query("select * from archivo where Proyecto_idProyecto = ?", [project.projectID], function(error, result) {

      if (error) {
        throw error;
      } else {
        socket.emit("filesReloaded", true, result);
      }
    });
  }

  function joinFilesRoom(file, connection) {
    room = "room-" + file.fileID;

    socket.leave(filesRooms[0]);
    filesRooms = [];

    socket.join(room);

    filesRooms.push(room);

    console.log("se unio al nuevo room de archivos que creo" + JSON.stringify(filesRooms));
    io.sockets.in(room).emit('connectToFilesRoom', filesRooms[0]);

  }

  function leaveFilesRoom(connection) {

    //var room = file.fileID;

    socket.leave(filesRooms[0]);
    console.log("rooms archivos antes " + JSON.stringify(filesRooms));
    //var index = filesRooms.findIndex(x => x.room==room);
    //filesRooms.splice(index, 1);

    filesRooms = [];

    console.log("rooms archivos despues " + JSON.stringify(filesRooms));
    //io.sockets.in(room).emit('disconnectFromFilesRoom', filesRooms);

  }

  function joinProjectRoom(connectedUser, connection) {

    user = connectedUser;
    room = connectedUser.projectID;
    var filteredRooms = [];

    console.log("se unio al nuevo room que creo");
    socket.join(room);
    users = {
      room: room,
      idUsuario: connectedUser.userID
    }
    projectsRooms.push(users);

    for (var i = projectsRooms.length - 1; i >= 0; i--) {
      if (projectsRooms[i].room == room) {
        filteredRooms.push(projectsRooms[i]);
      }
    }
    console.log("se unio al nuevo room que creo" + JSON.stringify(projectsRooms));
    io.sockets.in(room).emit('connectedToProjectRoom', filteredRooms);

  }

  function leaveProjectRoom(connectedUser, connection) {

    room = connectedUser.projectID;
    var filteredRooms = [];

    console.log("dejo el room");

    console.log("rooms antes " + JSON.stringify(projectsRooms));

    socket.leave(room);

    users = {
      room: room,
      idUsuario: connectedUser.userID
    }

    //var index = rooms.indexOf(users);
    var index = projectsRooms.findIndex(x => x.room == room & x.idUsuario == connectedUser.userID);

    console.log("indice del usuario a eliminar " + index);

    projectsRooms.splice(index, 1);

    console.log("rooms despues " + JSON.stringify(projectsRooms));

    for (var i = projectsRooms.length - 1; i >= 0; i--) {
      if (projectsRooms[i].room == room) {
        filteredRooms.push(projectsRooms[i]);
      }
    }

    console.log("rooms filtrados " + JSON.stringify(filteredRooms));
    io.sockets.in(room).emit('disconnectedFromProjectRoom', filteredRooms);

    console.log("room = " + room);

  }

  function recoverProjects(user, connection) {
    console.log([user.userID]);
    var query = connection.query("select * from proyecto where Usuario_idUsuario = ?", [user.userID], function(error, result) {

      if (error) {
        throw error;
      } else {
        socket.emit("projectsRecovered", true, result);
      }
    });
  }

  function saveProject(project, connection) {
    console.log([project.name]);
    var values = [project.name, project.programmingLanguage, project.userID];
    var query = connection.query("insert into proyecto(nombre, lenguaje, Usuario_idUsuario) values (?)", [values], function(error, result) {

      if (error) {
        throw error;
      } else {
        socket.emit("projectSaved", true, result.insertId);
      }
    });
  }

  function getCollaborators(projectID, connection) {
    console.log([projectID.projectID]);
    var query = connection.query("select idUsuario, alias, biografia from Usuario inner join Colaborador on idusuario=usuario_idusuario where proyecto_idProyecto= ?", [projectID.projectID], function(error, result) {

      if (error) {
        throw error;
      } else {
        socket.emit("collaboratorsRecovered", result);
      }
    });
  }

  function searchUser(criteria, connection) {
    console.log([criteria.searchCriteria]);
    var query = connection.query("select idUsuario, alias, biografia from Usuario where alias= ?", [criteria.searchCriteria], function(error, result) {

      if (error) {
        throw error;
      } else {
        if (result.length > 0) {
          if (result[0].alias == criteria.searchCriteria) {
            socket.emit("searchFinalized", true, result);
          } else {
            socket.emit("searchFinalized", false, "no coincidences");
          }
        } else {
          console.log("no entra");
          socket.emit("searchFinalized", false, "no existe el usuario");
        }
      }
    });
  }

  function saveCollaborator(collaboration, connection) {
    console.log([collaboration.collaboratorID]);
    var values = [collaboration.collaboratorID, collaboration.projectID];
    var query = connection.query("insert into colaborador(Usuario_idusuario, proyecto_idproyecto) values (?)", [values], function(error, result) {

      if (error) {
        throw error;
      } else {
        io.sockets.in(room).emit("collaborationSaved", collaboration.collaboratorID);
      }
    });
  }

  function deleteCollaborator(collaboration, connection) {
    console.log([collaboration.collaboratorID]);
    var values = [collaboration.collaboratorID, collaboration.projectID];
    var query = connection.query("delete from colaborador where Usuario_idusuario=? and proyecto_idproyecto=?", values, function(error, result) {

      if (error) {
        throw error;
      } else {
        io.sockets.in(room).emit("collaborationDeleted", collaboration.collaboratorID);
      }
    });
  }

  function deleteFile(file, connection) {
    console.log([file.fileID]);

    var query = connection.query("delete from archivo where idArchivo =?", [file.fileID], function(error, result) {

      if (error) {
        throw error;
      } else {
        socket.emit("fileDeleted", true);
      }
    });
  }

  function loadSharedProjects(user, connection) {
    console.log([user.userID]);

    var query = connection.query("select idProyecto, p.nombre, p.lenguaje from proyecto p inner join colaborador c on idProyecto=proyecto_idproyecto where c.usuario_idusuario = ?", [user.userID], function(error, result) {

      if (error) {
        throw error;
      } else {
        console.log(result);
        socket.emit("sharedProjectsRecovered", true, result);
      }
    });
  }

  function updateBiography(user, connection) {
    var query = connection.query("update usuario set biografia = ? where idUsuario = ?", [user.biography, user.userId], function(error, result) {
      if (error) {
        throw error;
      } else {
        socket.emit("biographySaved", true);
      }
    });
  }

});
