-- ================================================
-- BASE DE DATOS TPV TIENDA DE GUITARRAS
-- ================================================

DROP DATABASE IF EXISTS tpv_guitarras;
CREATE DATABASE tpv_guitarras CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE tpv_guitarras;

-- ================================================
-- TABLA: Empleados
-- ================================================
CREATE TABLE empleados (
    id_empleado INT PRIMARY KEY AUTO_INCREMENT,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    rol ENUM('ADMINISTRADOR', 'VENDEDOR') NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_alta TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ================================================
-- TABLA: Clientes
-- ================================================
CREATE TABLE clientes (
    id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    dni VARCHAR(15) UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(255),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ================================================
-- TABLA: Productos
-- ================================================
CREATE TABLE productos (
    id_producto INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(200) NOT NULL,
    marca VARCHAR(100) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL CHECK (precio >= 0),
    stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    tipo_producto ENUM('GUITARRA', 'AMPLIFICADOR', 'ACCESORIO') NOT NULL,
    descripcion TEXT,
    imagen_url VARCHAR(500),
    activo BOOLEAN DEFAULT TRUE,
    fecha_alta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_nombre (nombre),
    INDEX idx_tipo (tipo_producto)
);

-- ================================================
-- TABLA: Guitarras
-- ================================================
CREATE TABLE guitarras (
    id_guitarra INT PRIMARY KEY,
    tipo_guitarra ENUM('ELECTRICA', 'ACUSTICA', 'BAJO', 'ELECTROACUSTICA') NOT NULL,
    num_cuerdas INT NOT NULL DEFAULT 6,
    tipo_madera VARCHAR(100),
    color VARCHAR(50),
    num_trastes INT,
    FOREIGN KEY (id_guitarra) REFERENCES productos(id_producto) ON DELETE CASCADE
);

-- ================================================
-- TABLA: Amplificadores
-- ================================================
CREATE TABLE amplificadores (
    id_amplificador INT PRIMARY KEY,
    potencia_watts INT NOT NULL,
    num_canales INT NOT NULL DEFAULT 1,
    tipo_amplificador ENUM('VALVULAS', 'TRANSISTOR', 'MODELADO', 'HIBRIDO') NOT NULL,
    efectos_integrados BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_amplificador) REFERENCES productos(id_producto) ON DELETE CASCADE
);

-- ================================================
-- TABLA: Accesorios
-- ================================================
CREATE TABLE accesorios (
    id_accesorio INT PRIMARY KEY,
    categoria ENUM('CABLE', 'CORREA', 'CUERDAS', 'PUAS', 'PEDAL', 'FUNDA', 'SOPORTE', 'AFINADOR', 'OTRO') NOT NULL,
    material VARCHAR(100),
    compatible_con VARCHAR(100),
    FOREIGN KEY (id_accesorio) REFERENCES productos(id_producto) ON DELETE CASCADE
);

-- ================================================
-- TABLA: Ventas
-- ================================================
CREATE TABLE ventas (
    id_venta INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NULL,
    id_empleado INT NOT NULL,
    fecha_venta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10, 2) NOT NULL CHECK (total >= 0),
    metodo_pago ENUM('EFECTIVO', 'TARJETA', 'TRANSFERENCIA', 'MIXTO') NOT NULL,
    descuento DECIMAL(10, 2) DEFAULT 0.00,
    observaciones TEXT,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) ON DELETE SET NULL,
    FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado),
    INDEX idx_fecha (fecha_venta)
);

-- ================================================
-- TABLA: Lineas_Venta
-- ================================================
CREATE TABLE lineas_venta (
    id_linea INT PRIMARY KEY AUTO_INCREMENT,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(10, 2) NOT NULL CHECK (precio_unitario >= 0),
    subtotal DECIMAL(10, 2) NOT NULL CHECK (subtotal >= 0),
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

-- ================================================
-- DATOS: Empleados
-- ================================================
INSERT INTO empleados (usuario, contrasena, nombre, apellidos, rol) VALUES
('admin', '$2a$12$cOD84hVL2QYVobxsMDbdW.WYPTMnYwokF3MckKXZJPlPvWCyDWHvS', 'Carlos', 'García López', 'ADMINISTRADOR'),
('vendedor1', '$2a$12$lyaYpAULzksbliuNOJA6m.uwGCDA3rtV3WO01XSC7T.DGS25zRc4m', 'María', 'Fernández Ruiz', 'VENDEDOR'),
('vendedor2', '$2a$12$lyaYpAULzksbliuNOJA6m.uwGCDA3rtV3WO01XSC7T.DGS25zRc4m', 'Juan', 'Martínez Pérez', 'VENDEDOR'),
('admin2', '$2a$12$cOD84hVL2QYVobxsMDbdW.WYPTMnYwokF3MckKXZJPlPvWCyDWHvS', 'Luisa', 'Ramírez Torres', 'ADMINISTRADOR'),
('admin3', '$2a$12$cOD84hVL2QYVobxsMDbdW.WYPTMnYwokF3MckKXZJPlPvWCyDWHvS', 'Javier', 'Santos Molina', 'ADMINISTRADOR'),
('vendedor3', '$2a$12$lyaYpAULzksbliuNOJA6m.uwGCDA3rtV3WO01XSC7T.DGS25zRc4m', 'Ana', 'López Herrera', 'VENDEDOR'),
('vendedor4', '$2a$12$lyaYpAULzksbliuNOJA6m.uwGCDA3rtV3WO01XSC7T.DGS25zRc4m', 'Pedro', 'Sánchez Díaz', 'VENDEDOR'),
('vendedor5', '$2a$12$lyaYpAULzksbliuNOJA6m.uwGCDA3rtV3WO01XSC7T.DGS25zRc4m', 'Lucía', 'Moreno Campos', 'VENDEDOR'),
('vendedor6', '$2a$12$lyaYpAULzksbliuNOJA6m.uwGCDA3rtV3WO01XSC7T.DGS25zRc4m', 'Sergio', 'Navarro Ortiz', 'VENDEDOR'),
('vendedor7', '$2a$12$lyaYpAULzksbliuNOJA6m.uwGCDA3rtV3WO01XSC7T.DGS25zRc4m', 'Elena', 'Castro Vidal', 'VENDEDOR'),
('vendedor8', '$2a$12$lyaYpAULzksbliuNOJA6m.uwGCDA3rtV3WO01XSC7T.DGS25zRc4m', 'Roberto', 'Rey Carmona', 'VENDEDOR'),
('vendedor9', '$2a$12$lyaYpAULzksbliuNOJA6m.uwGCDA3rtV3WO01XSC7T.DGS25zRc4m', 'Patricia', 'Benítez Soto', 'VENDEDOR'),
('vendedor10', '$2a$12$lyaYpAULzksbliuNOJA6m.uwGCDA3rtV3WO01XSC7T.DGS25zRc4m', 'Héctor', 'Márquez Duarte', 'VENDEDOR');

-- ================================================
-- DATOS: Clientes
-- ================================================
INSERT INTO clientes (dni, nombre, apellidos, telefono, email, direccion) VALUES
('12345678A', 'Pedro', 'Sánchez Gómez', '600123456', 'pedro.sanchez@email.com', 'Calle Mayor 15, Madrid'),
('87654321B', 'Laura', 'Jiménez Torres', '600234567', 'laura.jimenez@email.com', 'Avenida Libertad 23, Barcelona'),
('11223344C', 'Miguel', 'Rodríguez López', '600345678', 'miguel.rodriguez@email.com', 'Plaza España 8, Valencia'),
('44556677D', 'Ana', 'Martínez Ruiz', '600456789', 'ana.martinez@email.com', 'Calle Sol 12, Sevilla'),
('99887766E', 'Javier', 'García Pérez', '600567890', 'javier.garcia@email.com', 'Avenida del Puerto 45, Bilbao'),
('55443322F', 'María', 'López Sánchez', '600678901', 'maria.lopez@email.com', 'Calle Luna 3, Zaragoza'),
('22334455G', 'Carlos', 'Hernández Torres', '600789012', 'carlos.hernandez@email.com', 'Calle Real 18, Málaga'),
('66778899H', 'Isabel', 'Gómez Martín', '600890123', 'isabel.gomez@email.com', 'Calle Jardines 27, Murcia'),
('33445566J', 'Roberto', 'Navarro Díaz', '600901234', 'roberto.navarro@email.com', 'Avenida Andalucía 52, Granada'),
('77889900K', 'Elena', 'Santos Romero', '600012345', 'elena.santos@email.com', 'Paseo del Río 9, Valladolid'),
('88990011L', 'Sergio', 'Iglesias Mora', '600123457', 'sergio.iglesias@email.com', 'Calle Norte 14, Vigo'),
('99001122M', 'Patricia', 'Calvo León', '600234568', 'patricia.calvo@email.com', 'Avenida Galicia 33, A Coruña'),
('10111213N', 'David', 'Vicente Rubio', '600345679', 'david.vicente@email.com', 'Calle Alameda 7, Santander'),
('12131415P', 'Lucía', 'Rey Sanz', '600456780', 'lucia.rey@email.com', 'Paseo Colón 22, Pamplona'),
('13141516Q', 'Hugo', 'Molina Crespo', '600567891', 'hugo.molina@email.com', 'Calle Toledo 5, Toledo'),
('14151617R', 'Sara', 'Ortega Lozano', '600678902', 'sara.ortega@email.com', 'Calle Duque 41, Burgos'),
('15161718S', 'Alberto', 'Morales Herrera', '600789013', 'alberto.morales@email.com', 'Avenida Castellana 99, Madrid'),
('16171819T', 'Cristina', 'Vidal Peña', '600890124', 'cristina.vidal@email.com', 'Calle Nueva 2, Logroño'),
('17181920U', 'Jorge', 'Serrano Gil', '600901235', 'jorge.serrano@email.com', 'Calle Mayor 11, Salamanca'),
('18192021V', 'Natalia', 'Ramos Rivas', '600012356', 'natalia.ramos@email.com', 'Avenida Europa 66, Córdoba'),
('19202122W', 'Andrés', 'Domingo Soler', '600123468', 'andres.domingo@email.com', 'Calle Paz 19, Alicante'),
('20212223X', 'Beatriz', 'Bravo Carmona', '600234579', 'beatriz.bravo@email.com', 'Paseo Marítimo 5, Cádiz'),
('21222324Y', 'Pablo', 'Sáez Ferrer', '600345680', 'pablo.saez@email.com', 'Calle Sierra 10, Huesca'),
('22232425Z', 'Marta', 'Camacho Vega', '600456781', 'marta.camacho@email.com', 'Avenida Sol 30, Jaén'),
('23242526A', 'Fernando', 'Rivas Montes', '600567892', 'fernando.rivas@email.com', 'Calle Molino 3, Cuenca'),
('24252627B', 'Claudia', 'Estevez Lara', '600678903', 'claudia.estevez@email.com', 'Calle San Juan 50, León'),
('25262728C', 'Iván', 'Pastor Aguilar', '600789014', 'ivan.pastor@email.com', 'Avenida Centro 12, Badajoz'),
('26272829D', 'Rocío', 'Fuentes Alba', '600890125', 'rocio.fuentes@email.com', 'Calle Olivo 8, Almería');

-- ================================================
-- DATOS: Productos GUITARRAS
-- ================================================
INSERT INTO productos (nombre, marca, precio, stock, tipo_producto, descripcion, imagen_url) VALUES
('Stratocaster American Professional II', 'Fender', 1899.00, 5, 'GUITARRA', 'Guitarra eléctrica profesional con pastillas V-Mod II', 'https://www.fender.com/cdn-cgi/image/format=auto,resize=height=auto,width=1500/https://www.fmicassets.com/Damroot/eCommPNG/10001/0113900700_fen_ins_frt_1_rr.png'),
('Les Paul Standard 60s', 'Gibson', 2499.00, 3, 'GUITARRA', 'Guitarra eléctrica con cuerpo de caoba y tapa de arce', 'https://www.gibson.com/cdn/shop/files/LPS6P25TONH3_1_Body.webp?v=1751914921&width=1100'),
('RG550 Genesis', 'Ibanez', 1299.00, 8, 'GUITARRA', 'Guitarra eléctrica versátil con trémolo Edge', 'https://www.ibanez.com/common/product_artist_file/file/p_region_RG550_BK_00_04.png'),
('Telecaster Player Series', 'Fender', 799.00, 10, 'GUITARRA', 'Guitarra eléctrica clásica para todo tipo de música', 'https://www.fender.com/cdn-cgi/image/format=auto,resize=height=auto,width=1500/https://www.fmicassets.com/Damroot/eCommPNG/10043/0140552550_fen_ins_frt_1_rr.png'),
('Precision Bass', 'Fender', 1599.00, 4, 'GUITARRA', 'Bajo eléctrico de 4 cuerdas, sonido clásico', 'https://www.fender.com/cdn-cgi/image/format=auto,resize=height=auto,width=1500/https://www.fmicassets.com/Damroot/eCommPNG/10083/0147492310_fen_ins_frt_1_rr.png'),
('D-28 Acústica', 'Martin', 3299.00, 2, 'GUITARRA', 'Guitarra acústica premium con tapa de abeto', 'https://www.martinguitar.com/dw/image/v2/BGJT_PRD/on/demandware.static/-/Sites-martin-master-catalog/default/dwd58bd1b3/images/D-28/Y25D28/Y25D28_f.jpg?sw=1600&sh=1600&sm=fit'),
('SG Standard', 'Gibson', 1799.00, 4, 'GUITARRA', 'Guitarra eléctrica icónica con doble cutaway y pastillas humbucker', 'https://www.gibson.com/cdn/shop/files/SG61F5725AYNH3_211550275_1_Body.webp?v=1751914922&width=990'),
('Custom 24', 'PRS', 3499.00, 2, 'GUITARRA', 'Guitarra premium con tapa de arce flameado y pastillas propias', 'https://www.musicalpontevedra.es/52944-large_default/guitarra-electrica-prs-custom-24-bg.jpg'),
('Jazzmaster Player Series', 'Fender', 899.00, 6, 'GUITARRA', 'Guitarra offset clásica, ideal para surf y rock alternativo', 'https://www.fender.com/cdn-cgi/image/format=auto,resize=height=auto,width=1500/https://www.fmicassets.com/Damroot/eCommPNG/10040/0140590518_fen_ins_frt_1_rr.png'),
('Flying V', 'Gibson', 2199.00, 2, 'GUITARRA', 'Guitarra eléctrica con diseño radical y sonido potente', 'https://www.gibson.com/cdn/shop/files/DSVE00EBCH1_front.png?v=1744820001&width=823'),
('Steve Vai Signature JEM', 'Ibanez', 2899.00, 3, 'GUITARRA', 'Guitarra de alta gama con trémolo flotante Edge', 'https://www.ibanez.com/common/product_artist_file/file/p_region_RG550_DY_00_04.png'),
('Superstrat Pro', 'Charvel', 1499.00, 5, 'GUITARRA', 'Guitarra moderna para shred y metal con floyd rose', 'https://www.fmicassets.com/Damroot/Zoom/10072/2965801368_cha_ins_frt_1_rr.png'),
('Explorer', 'Gibson', 1899.00, 3, 'GUITARRA', 'Guitarra con cuerpo angular, sonido contundente', 'https://www.gibson.com/cdn/shop/files/58KEXVOGH1_front_241cf99c-6495-406f-8167-017e573f6a58.webp?v=1744040876&width=823'),
('S Series Prestige', 'Ibanez', 1699.00, 4, 'GUITARRA', 'Guitarra ultradelgada y ergonómica para virtuosos', 'https://www.ibanez.com/common/product_artist_file/file/ps_main_eg_s_prestige_en.png'),
('Musicman StingRay', 'Music Man', 1999.00, 3, 'GUITARRA', 'Bajo eléctrico de 4 cuerdas con pastilla humbucker activa', 'https://s3-us-west-2.amazonaws.com/static.music-man.com/website/images/instrument_colors/image_swap/full/519.png?1752027144'),
('Jazz Bass', 'Fender', 1799.00, 4, 'GUITARRA', 'Bajo versátil con dos pastillas single coil', 'https://www.fender.com/cdn-cgi/image/format=auto,resize=height=auto,width=1500/https://www.fmicassets.com/Damroot/eCommPNG/10013/0190170800_fen_ins_frt_1_rr.png'),
('Thunderbird', 'Gibson', 1599.00, 2, 'GUITARRA', 'Bajo con diseño característico y sonido profundo', 'https://www.gibson.com/cdn/shop/files/EIGTB6EMRNH3_front.png?v=1745527173&width=823'),
('SR Premium 5 cuerdas', 'Ibanez', 1399.00, 3, 'GUITARRA', 'Bajo de 5 cuerdas con electrónica activa', 'https://thumbs.static-thomann.de/thumb/thumb80x80/pics/bdb/_60/606656/19698304_800.jpg'),
('AJ220SCE', 'Epiphone', 549.00, 7, 'GUITARRA', 'Guitarra electroacústica con cutaway y preamplificador', 'https://www.madridhifi.com/_next/image/?url=https%3A%2F%2Fdolibarr-new-prod.madridhifi.com%2Fcrm%2Fdocuments%2Fproduit%2F9%2F0%2F10173009%2Fphotos%2Fdetalle%2Fepiphone-j-45-ec-studio-vintage-sunburst-comprar.jpg&w=1080&q=75'),
('J-45 Standard', 'Gibson', 2799.00, 2, 'GUITARRA', 'Guitarra acústica legendaria con sonido cálido', 'https://www.gibson.com/cdn/shop/files/RS45VSN19_front.webp?v=1752155914&width=823'),
('GS Mini', 'Taylor', 599.00, 8, 'GUITARRA', 'Guitarra acústica compacta perfecta para viajes', 'https://www.taylorguitars.com/sites/default/files/styles/guitar_desktop/public/images/2025-02/Taylor-GS-Mini-Sapele-2210303115-FrontLeft-2023.png?itok=eCJ6-xrM'),
('214ce-DLX', 'Taylor', 1299.00, 5, 'GUITARRA', 'Guitarra electroacústica con tapa de koa', 'https://www.taylorguitars.com/sites/default/files/styles/guitar_desktop/public/images/2025-04/Taylor-Sunset-Blvd-214ce-DLX-SEB-2202175266-FrontLeft-2025.png?itok=ob-V1sDu'),
('APX600', 'Yamaha', 399.00, 10, 'GUITARRA', 'Guitarra electroacústica slim line para conciertos', 'https://thumbs.static-thomann.de/thumb/thumb80x80/pics/bdb/_43/432403/13081441_800.jpg'),
('Hummingbird', 'Gibson', 3999.00, 1, 'GUITARRA', 'Guitarra acústica vintage con golpeador característico', 'https://www.gibson.com/cdn/shop/files/CSSSHB60FBHCS_front.webp?v=1745941369&width=823');

INSERT INTO guitarras (id_guitarra, tipo_guitarra, num_cuerdas, tipo_madera, color, num_trastes) VALUES
(1, 'ELECTRICA', 6, 'Aliso', 'Sunburst', 22),
(2, 'ELECTRICA', 6, 'Caoba', 'Tobacco Burst', 22),
(3, 'ELECTRICA', 6, 'Tilo', 'Negro', 24),
(4, 'ELECTRICA', 6, 'Aliso', 'Rubio', 22),
(5, 'BAJO', 4, 'Aliso', 'Negro', 20),
(6, 'ACUSTICA', 6, 'Abeto/Palisandro', 'Natural', 20),
(7, 'ELECTRICA', 6, 'Caoba', 'Cherry Red', 22),
(8, 'ELECTRICA', 6, 'Caoba/Arce', 'Flame Top', 24),
(9, 'ELECTRICA', 6, 'Aliso', 'Sonic Blue', 22),
(10, 'ELECTRICA', 6, 'Caoba', 'Ebony', 22),
(11, 'ELECTRICA', 6, 'Tilo/Arce', 'Monkey Grip', 24),
(12, 'ELECTRICA', 6, 'Aliso', 'Negro Mate', 24),
(13, 'ELECTRICA', 6, 'Caoba', 'Natural', 22),
(14, 'ELECTRICA', 6, 'Basswood', 'Sunburst', 24),
(15, 'BAJO', 4, 'Fresno', 'Natural', 21),
(16, 'BAJO', 4, 'Aliso', 'Sunburst', 20),
(17, 'BAJO', 4, 'Caoba', 'Ember Red', 20),
(18, 'BAJO', 5, 'Caoba', 'Brown Sunburst', 24),
(19, 'ELECTROACUSTICA', 6, 'Abeto/Palisandro', 'Natural', 20),
(20, 'ACUSTICA', 6, 'Abeto Sitka', 'Vintage Sunburst', 20),
(21, 'ACUSTICA', 6, 'Abeto/Sapeli', 'Natural', 20),
(22, 'ELECTROACUSTICA', 6, 'Koa', 'Natural', 20),
(23, 'ELECTROACUSTICA', 6, 'Abeto/Nato', 'Negro', 21),
(24, 'ACUSTICA', 6, 'Abeto/Palisandro', 'Cherry Sunburst', 20);

-- ================================================
-- DATOS: Productos AMPLIFICADORES
-- ================================================
INSERT INTO productos (nombre, marca, precio, stock, tipo_producto, descripcion, imagen_url) VALUES
('Blues Junior IV', 'Fender', 599.00, 6, 'AMPLIFICADOR', 'Amplificador de válvulas 15W, sonido clásico', 'https://www.fender.com/cdn-cgi/image/format=auto,resize=height=auto,width=1500/https://www.fmicassets.com/Damroot/eCommPNG/10036/2231506000_amp_frt_1_nr.png'),
('DSL40CR', 'Marshall', 749.00, 4, 'AMPLIFICADOR', 'Amplificador de válvulas 40W, dos canales', 'https://thumbs.static-thomann.de/thumb/padthumb600x600/pics/bdb/_42/422013/12894607_800.jpg'),
('Katana-50 MkII', 'Boss', 299.00, 12, 'AMPLIFICADOR', 'Amplificador de modelado 50W con efectos', 'https://www.unionmusical.es/media/catalog/product/7/1/713682_2701.jpg'),
('THR10II', 'Yamaha', 349.00, 8, 'AMPLIFICADOR', 'Amplificador de escritorio con bluetooth', 'https://es.yamaha.com/es/files/Image-Index_THR-II_THR30II_Wireless_1080x1080_tcm121-1662789.jpg?impolicy=resize&imwid=735&imhei=735'),
('JVM410H Cabezal', 'Marshall', 2199.00, 2, 'AMPLIFICADOR', 'Cabezal de válvulas 100W, 4 canales programables', 'https://images.ctfassets.net/javen7msabdh/15E59g3brZSEKyd1y0ROcZ/00d3674f289544fb487450667dd1be0a/jvm410h-full-width-desktop-02.png?w=2400&fm=avif&q=100'),
('Twin Reverb', 'Fender', 1499.00, 3, 'AMPLIFICADOR', 'Amplificador de válvulas 85W estéreo, sonido limpio legendario', 'https://www.fender.com/cdn-cgi/image/format=auto,resize=height=auto,width=1500/https://www.fmicassets.com/Damroot/eCommPNG/10003/0217300000_amp_frt_001_nr.png'),
('AC30C2', 'Vox', 1299.00, 4, 'AMPLIFICADOR', 'Amplificador de válvulas 30W con 2 altavoces 12"', 'https://voxamps.com/wp-content/uploads/2019/01/AC30C2_2_resized.png'),
('Dual Rectifier', 'Mesa Boogie', 2899.00, 2, 'AMPLIFICADOR', 'Amplificador de alta ganancia 100W para metal', 'https://sc1.musik-produktiv.com/pic-010045241s/mesa-boogie-dual-rectifier.jpg'),
('Katana-100 MkII', 'Boss', 449.00, 8, 'AMPLIFICADOR', 'Amplificador de modelado 100W con librería de efectos', 'https://www.malaga8.com/80738-large_default/boss-katana-100-gen-3.jpg'),
('Code 50', 'Marshall', 399.00, 6, 'AMPLIFICADOR', 'Amplificador digital 50W con presets y app móvil', 'https://www.madridhifi.com/_next/image/?url=https%3A%2F%2Fdolibarr-new-prod.madridhifi.com%2Fcrm%2Fdocuments%2Fproduit%2F2%2F3%2F10100032%2Fphotos%2Fdetalle%2FMarshall-code-50-combo.png&w=1920&q=75'),
('Spider V 240 MkII', 'Line 6', 549.00, 5, 'AMPLIFICADOR', 'Amplificador de modelado 240W con altavoz 12"', 'https://www.malaga8.com/49986-large_default/line-6-spider-v-240-mkii-p-33203.jpg'),
('Crush 35RT', 'Orange', 329.00, 7, 'AMPLIFICADOR', 'Amplificador transistor 35W con reverb y afinador', 'https://thumbs.static-thomann.de/thumb/thumb80x80/pics/bdb/_35/356303/14129687_800.jpg'),
('Rumble 500', 'Fender', 649.00, 4, 'AMPLIFICADOR', 'Amplificador para bajo 500W, ligero y potente', 'https://www.fender.com/cdn-cgi/image/format=auto,resize=height=auto,width=1500/https://www.fmicassets.com/Damroot/eCommPNG/10002/2370600000_amp_frt_001_nr.png'),
('SVT-3 Pro', 'Ampeg', 899.00, 3, 'AMPLIFICADOR', 'Cabezal para bajo 450W, sonido clásico', 'https://www.ardemadrid.com/22361-large_default/ampeg-svt-3-pro.jpg'),
('Acoustic 40', 'Fender', 249.00, 8, 'AMPLIFICADOR', 'Amplificador para guitarra acústica 40W con efectos', 'https://www.fender.com/cdn-cgi/image/format=auto,resize=height=auto,width=1500/https://www.fmicassets.com/Damroot/eCommPNG/10002/2314200000_amp_frt_001_nr.png'),
('Frontman 10G', 'Fender', 79.00, 15, 'AMPLIFICADOR', 'Amplificador de práctica 10W, ideal principiantes', 'https://www.fender.com/cdn-cgi/image/format=auto,resize=height=auto,width=1500/https://www.fmicassets.com/Damroot/eCommPNG/10004/2311000000_fen_amp_frt_1_nr.png'),
('MG15GFX', 'Marshall', 89.00, 12, 'AMPLIFICADOR', 'Amplificador compacto 15W con efectos integrados', 'https://images.ctfassets.net/javen7msabdh/BDomMgPWMKi4MEJwi3agm/932d14c3439f2a5e0b485ea64c5c149e/mg15fx-two-asset-hybrid-04.png?w=1440&fm=avif&q=100'),
('Micro Terror', 'Orange', 149.00, 10, 'AMPLIFICADOR', 'Cabezal híbrido ultracompacto 20W', 'https://r2.gear4music.com/media/50/502019/600/preview.jpg'),
('Stamp 12', 'BluGuitar', 549.00, 4, 'AMPLIFICADOR', 'Amplificador de válvulas digital ultracompacto 40W', 'https://thumbs.static-thomann.de/thumb/thumb80x80/pics/bdb/_36/368797/10625367_800.jpg'),
('HT-5R MkII', 'Blackstar', 399.00, 6, 'AMPLIFICADOR', 'Amplificador de válvulas 5W con reverb y ISF', 'https://es.blackstaramps.com/wp-content/uploads/sites/3/2021/05/HT-5R-Mkll-result-image.jpg');

INSERT INTO amplificadores (id_amplificador, potencia_watts, num_canales, tipo_amplificador, efectos_integrados) VALUES
(25, 15, 1, 'VALVULAS', FALSE),
(26, 40, 2, 'VALVULAS', TRUE),
(27, 50, 4, 'MODELADO', TRUE),
(28, 10, 5, 'MODELADO', TRUE),
(29, 100, 4, 'VALVULAS', TRUE),
(30, 85, 2, 'VALVULAS', TRUE),
(31, 30, 2, 'VALVULAS', TRUE),
(32, 100, 3, 'VALVULAS', TRUE),
(33, 100, 4, 'MODELADO', TRUE),
(34, 50, 4, 'MODELADO', TRUE),
(35, 240, 4, 'MODELADO', TRUE),
(36, 35, 2, 'TRANSISTOR', TRUE),
(37, 500, 2, 'TRANSISTOR', FALSE),
(38, 450, 1, 'VALVULAS', TRUE),
(39, 40, 2, 'TRANSISTOR', TRUE),
(40, 10, 1, 'TRANSISTOR', FALSE),
(41, 15, 2, 'TRANSISTOR', TRUE),
(42, 20, 1, 'HIBRIDO', FALSE),
(43, 40, 1, 'VALVULAS', TRUE),
(44, 5, 2, 'VALVULAS', TRUE);

-- ================================================
-- DATOS: Productos ACCESORIOS
-- ================================================
INSERT INTO productos (nombre, marca, precio, stock, tipo_producto, descripcion, imagen_url) VALUES
('Cable Jack 6m Profesional', 'Planet Waves', 29.99, 25, 'ACCESORIO', 'Cable jack-jack 6 metros, baja capacitancia', 'https://www.madridhifi.com/_next/image/?url=https%3A%2F%2Fdolibarr-new-prod.madridhifi.com%2Fcrm%2Fdocuments%2Fproduit%2F5%2F5%2F10177955%2Fphotos%2Fdetalle%2Fplanet-waves-pw-cgtra-20.jpg&w=1920&q=75'),
('Correa Acolchada', 'Ernie Ball', 19.99, 30, 'ACCESORIO', 'Correa ajustable con acolchado', 'https://s3.us-west-2.amazonaws.com/static.ernieball.com/website/images/products/image_front/large/P05373.png'),
('Juego Cuerdas 10-46', 'D''Addario', 9.99, 50, 'ACCESORIO', 'Cuerdas níquel para guitarra eléctrica', 'https://www.madridhifi.com/_next/image/?url=https%3A%2F%2Fdolibarr-new-prod.madridhifi.com%2Fcrm%2Fdocuments%2Fproduit%2F2%2F4%2F10131442%2Fphotos%2Fdetalle%2Fdaddario-exl110-xl-MAIN.png&w=1920&q=75'),
('Afinador Cromático Pedal', 'TC Electronic', 89.00, 15, 'ACCESORIO', 'Afinador pedal con true bypass', 'https://www.unionmusical.es/media/catalog/product/6/6/664026_1_40b4.jpg'),
('Funda Guitarra Eléctrica', 'Gator', 79.00, 10, 'ACCESORIO', 'Funda acolchada con bolsillos', 'https://thumbs.static-thomann.de/thumb/thumb80x80/pics/bdb/_34/346369/18825664_800.jpg'),
('Púas Variadas Pack 12', 'Dunlop', 5.99, 100, 'ACCESORIO', 'Pack de 12 púas de diferentes grosores', 'https://thumbs.static-thomann.de/thumb/thumb80x80/pics/bdb/_41/419335/12413222_800.jpg');

INSERT INTO accesorios (id_accesorio, categoria, material, compatible_con) VALUES
(45, 'CABLE', 'Cobre OFC', 'Guitarra/Bajo'),
(46, 'CORREA', 'Polipropileno', 'Guitarra/Bajo'),
(47, 'CUERDAS', 'Níquel', 'Guitarra Eléctrica'),
(48, 'AFINADOR', 'Metal/Plástico', 'Universal'),
(49, 'FUNDA', 'Nylon', 'Guitarra Eléctrica'),
(50, 'PUAS', 'Nylon', 'Guitarra/Bajo');

-- ================================================
-- VISTAS
-- ================================================
CREATE VIEW vista_productos_completos AS
SELECT
    p.id_producto,
    p.nombre,
    p.marca,
    p.precio,
    p.stock,
    p.tipo_producto,
    p.descripcion,
    CASE
        WHEN p.tipo_producto = 'GUITARRA' THEN CONCAT(g.tipo_guitarra, ' - ', g.num_cuerdas, ' cuerdas - ', g.color)
        WHEN p.tipo_producto = 'AMPLIFICADOR' THEN CONCAT(a.potencia_watts, 'W - ', a.tipo_amplificador)
        WHEN p.tipo_producto = 'ACCESORIO' THEN CONCAT(ac.categoria, ' - ', ac.material)
    END AS detalles
FROM productos p
LEFT JOIN guitarras g ON p.id_producto = g.id_guitarra
LEFT JOIN amplificadores a ON p.id_producto = a.id_amplificador
LEFT JOIN accesorios ac ON p.id_producto = ac.id_accesorio
WHERE p.activo = TRUE;

CREATE VIEW vista_ventas_completas AS
SELECT
    v.id_venta,
    v.fecha_venta,
    v.total,
    v.metodo_pago,
    CONCAT(e.nombre, ' ', e.apellidos) AS vendedor,
    IFNULL(CONCAT(c.nombre, ' ', c.apellidos), 'Cliente Ocasional') AS cliente,
    c.telefono,
    c.email
FROM ventas v
INNER JOIN empleados e ON v.id_empleado = e.id_empleado
LEFT JOIN clientes c ON v.id_cliente = c.id_cliente;

-- ================================================
-- PROCEDIMIENTOS ALMACENADOS
-- ================================================
CREATE PROCEDURE realizar_venta(
    IN p_id_cliente INT,
    IN p_id_empleado INT,
    IN p_metodo_pago VARCHAR(20),
    IN p_descuento DECIMAL(10,2),
    OUT p_id_venta INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_id_venta = -1;
    END;
    START TRANSACTION;
    INSERT INTO ventas (id_cliente, id_empleado, total, metodo_pago, descuento)
    VALUES (p_id_cliente, p_id_empleado, 0, p_metodo_pago, p_descuento);
    SET p_id_venta = LAST_INSERT_ID();
    COMMIT;
END;

CREATE PROCEDURE agregar_linea_venta(
    IN p_id_venta INT,
    IN p_id_producto INT,
    IN p_cantidad INT
)
BEGIN
    DECLARE v_precio DECIMAL(10,2);
    DECLARE v_subtotal DECIMAL(10,2);
    DECLARE v_stock_actual INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;
    START TRANSACTION;
    SELECT precio, stock INTO v_precio, v_stock_actual
    FROM productos
    WHERE id_producto = p_id_producto AND activo = TRUE;
    IF v_stock_actual < p_cantidad THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock insuficiente';
    END IF;
    SET v_subtotal = v_precio * p_cantidad;
    INSERT INTO lineas_venta (id_venta, id_producto, cantidad, precio_unitario, subtotal)
    VALUES (p_id_venta, p_id_producto, p_cantidad, v_precio, v_subtotal);
    UPDATE productos SET stock = stock - p_cantidad WHERE id_producto = p_id_producto;
    UPDATE ventas SET total = (SELECT SUM(subtotal) FROM lineas_venta WHERE id_venta = p_id_venta)
    WHERE id_venta = p_id_venta;
    COMMIT;
END;

-- ================================================
-- VISTAS DE REPORTES
-- ================================================
CREATE VIEW reporte_productos_mas_vendidos AS
SELECT
    p.nombre,
    p.marca,
    SUM(lv.cantidad) AS unidades_vendidas,
    SUM(lv.subtotal) AS ingresos_totales
FROM lineas_venta lv
INNER JOIN productos p ON lv.id_producto = p.id_producto
GROUP BY p.id_producto, p.nombre, p.marca
ORDER BY unidades_vendidas DESC
LIMIT 10;

CREATE VIEW reporte_ventas_diarias AS
SELECT
    DATE(fecha_venta) AS fecha,
    COUNT(*) AS num_ventas,
    SUM(total) AS total_dia
FROM ventas
WHERE fecha_venta >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
GROUP BY DATE(fecha_venta)
ORDER BY fecha DESC;

CREATE VIEW alerta_stock_bajo AS
SELECT
    id_producto,
    nombre,
    marca,
    stock,
    tipo_producto
FROM productos
WHERE stock < 5 AND activo = TRUE
ORDER BY stock ASC;

-- ================================================
-- INDICES ADICIONALES
-- ================================================
CREATE INDEX idx_ventas_fecha ON ventas(fecha_venta);
CREATE INDEX idx_ventas_empleado ON ventas(id_empleado);
CREATE INDEX idx_lineas_producto ON lineas_venta(id_producto);

-- ================================================
-- FIN DEL SCRIPT
-- ================================================
SELECT 'Base de datos TPV_GUITARRAS creada exitosamente' AS mensaje;