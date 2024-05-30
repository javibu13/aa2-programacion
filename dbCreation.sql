-- Tabla Usuario
CREATE TABLE Usuario (
    Id INT AUTO_INCREMENT,
    Nombre VARCHAR(255) NOT NULL,
    Correo VARCHAR(255) NOT NULL,
    Password VARCHAR(40) NOT NULL,  -- SHA1 produce una cadena de 40 caracteres
    AvatarPath VARCHAR(255) NULL,
    Rol ENUM('Administrador', 'Usuario') NOT NULL,

    CONSTRAINT PK_Usuario PRIMARY KEY (Id),
    CONSTRAINT UQ_Correo UNIQUE (Correo)  -- Asegurarse que el correo sea único
);

-- Tabla Producto
CREATE TABLE Producto (
    Id VARCHAR(50),
    Nombre VARCHAR(255) NOT NULL,
    Descripcion VARCHAR(500) NOT NULL,
    LimiteUso INT NOT NULL,  -- LimiteUso en meses

    CONSTRAINT PK_Producto PRIMARY KEY (Id)
);

-- Tabla Elemento
CREATE TABLE Elemento (
    Id INT AUTO_INCREMENT,
    ProductoId VARCHAR(50) NOT NULL,
    NumSerie VARCHAR(255) NOT NULL,
    Estado ENUM('Bueno', 'Regular', 'Malo') NOT NULL,

    CONSTRAINT PK_Elemento PRIMARY KEY (Id),
    CONSTRAINT UQ_ProductoId_NumSerie UNIQUE (ProductoId, NumSerie),  -- Combinación única de Producto y NumSerie
    CONSTRAINT FK_Elemento_Producto FOREIGN KEY (ProductoId) REFERENCES Producto(Id)
);

-- Tabla Asignacion
CREATE TABLE Asignacion (
    Id INT AUTO_INCREMENT,
    ElementoId INT NOT NULL,
    UsuarioId INT NOT NULL,
    FechaAsignacion DATE NOT NULL,
    FechaDevolucion DATE NULL,

    CONSTRAINT PK_Asignacion PRIMARY KEY (Id),
    CONSTRAINT FK_Asignacion_Elemento FOREIGN KEY (ElementoId) REFERENCES Elemento(Id),
    CONSTRAINT FK_Asignacion_Usuario FOREIGN KEY (UsuarioId) REFERENCES Usuario(Id)
);