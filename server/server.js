const express = require('express');
const http = require('http');
const socketIo = require('socket.io');
const firebaseAdmin = require('firebase-admin');
const cors = require('cors');

const app = express();
const server = http.createServer(app);
const io = socketIo(server);

// Configura Firebase
firebaseAdmin.initializeApp({
    credential: firebaseAdmin.credential.cert('C:\\LastShot\\Proyecto_Last_Shot\\app\\google-services.json')
});

const db = firebaseAdmin.firestore();

// Middleware CORS
app.use(cors());

// Ruta para verificar que el servidor funciona
app.get('/', (req, res) => {
    res.send('Servidor funcionando');
});

// Configurar socket.io
io.on('connection', (socket) => {
    console.log('Un jugador se ha conectado');

    socket.on('crearSala', (salaData) => {
        // Crear una nueva sala en Firebase
        const salaRef = db.collection('salas').doc();
        salaRef.set({
            nombre: salaData.nombreSala,
            clave: salaData.clave,
            jugadores: []
        }).then(() => {
            console.log('Sala creada');
            socket.emit('salaCreada', { salaId: salaRef.id });
        }).catch(err => {
            console.error('Error al crear la sala', err);
            socket.emit('error', 'Error al crear la sala');
        });
    });

    socket.on('unirseSala', (clave) => {
        // Buscar sala por clave
        db.collection('salas').where('clave', '==', clave).get()
            .then(snapshot => {
                if (snapshot.empty) {
                    socket.emit('error', 'No se encontró la sala');
                    return;
                }
                const sala = snapshot.docs[0];
                socket.emit('salaUnida', { salaId: sala.id, jugadores: sala.data().jugadores });
            })
            .catch(err => {
                console.error('Error al unirse a la sala', err);
                socket.emit('error', 'Error al unirse a la sala');
            });
    });
    
    socket.on('disconnect', () => {
        console.log('Jugador desconectado');
    });
});

// Iniciar el servidor en el puerto 3000
server.listen(3000, () => {
    console.log('Servidor escuchando en http://localhost:3000');
});
