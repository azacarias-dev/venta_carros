# VentaCarros

Aplicación de escritorio construida con Java y JavaFX que simula una tienda de ventas de carros. Permite la gestión básica de clientes, productos (vehículos) y compras.

## Tecnologías Utilizadas

- Java 21
- JavaFX 23.0.2
- MySQL 5.1.47
- CSS para el diseño de interfaces
- Maven como sistema de construcción

---

## Características

- Registro e inicio de sesión de clientes.
- Gestión de clientes (Agregar, listar, actualizar, "eliminar" lógicamente).
- Gestión de productos (carros): alta, baja, modificación y listado.
- Registro y visualización de compras.
- Estilo visual moderno y responsivo.

---

## Requisitos

- JDK 21+
- Maven 3+
- Servidor MySQL activo

---

## Configuración Inicial de la Base de Datos

La aplicación depende de una base de datos llamada `VentaCarrosDB`. Puedes crearla con el siguiente script:

```sql
create database VentaCarrosDB;
use VentaCarrosDB;

create table Clientes(
	idCliente int auto_increment,
	nombre varchar (64) not null,
	apellido varchar(64) not null,
	telefono varchar (16) not null,
	correo varchar(128) not null,
	nit varchar(16) not null,
	estado varchar(32) default "Activo",
    contraseña varchar(64) not null,
    constraint pk_clientes primary key (idCliente)
);


create table Productos(
	idProducto int auto_increment,
	nombre varchar (60) not null,
	precio decimal(10,2) not null,
	descripcion varchar (80) not null,
	stock int,
    estado varchar(32) default "Existente",
	constraint pk_productos primary Key (idProducto)
);

create table Compras(
	idCompra int auto_increment,
    idCliente int not null,
    fechaCompra date not null,
    idProducto int not null,
    cantidad int not null,
    subtotal decimal (10,2) not null,
    constraint pk_compras primary key (idCompra),
    constraint fk_compras_productos foreign key (idProducto)
		references Productos(idProducto),
	constraint fk_compras_clientes foreign key (idCliente)
		references Clientes(idCliente)
);

-- =================================== Procedimientos Almacenados==========================================
-- ==========Clientes============

delimiter //
	create procedure sp_listarClientes()
		begin
			select *
            from Clientes;
		end//
delimiter ;


delimiter //
	create procedure sp_agregarClientes(
		in p_nombre varchar(64),
        in p_apellido varchar(64),
        in p_telefono varchar(16),
        in p_correo varchar(126),
        in p_nit varchar(16),
        in p_contraseña varchar(64))
			begin
					insert into Clientes(nombre, apellido, telefono, correo, nit, contraseña)
                    values(p_nombre, p_apellido, p_telefono, p_correo, p_nit, p_contraseña);
            end//
delimiter ;


delimiter //
	create procedure sp_actualizarClientes(
		in p_idCliente int,
		in p_nombre varchar(64),
        in p_apellido varchar(64),
        in p_telefono varchar(16),
        in p_correo varchar(126),
        in p_nit varchar(16),
        in p_contraseña varchar(64))
		begin
			update Clientes C
				set
					C.nombre = p_nombre,
                    C.apellido = p_apellido,
                    C.telefono = p_telefono,
                    C.correo = p_correo,
                    C.nit = p_nit,
                    C.contraseña = p_contraseña
				where C.idCliente = p_idCliente;
        end//
delimiter ;

delimiter //
	create procedure sp_eliminarClientes(in p_idCliente int)
		begin
			update Clientes C
				set
					C.estado = "Inactivo"
				where C.idCliente = p_idCliente;
        end//
delimiter ;

-- ==========Productos==============

delimiter //
	create procedure sp_listarProductos()
		begin
			select *
            from Productos;
		end//
delimiter ;

delimiter //
	create procedure sp_agregarProductos(
		in p_nombre varchar(60),
        in p_precio decimal(10,2),
        in p_descripcion varchar(80),
        in p_stock int)
			begin
					insert into Productos(nombre, precio, descripcion, stock)
                    values(p_nombre, p_precio, p_descripcion, p_stock);
            end//
delimiter ;


delimiter //
	create procedure sp_actualizarProductos(
		in p_idProducto int,
		in p_nombre varchar(60),
        in p_precio decimal(10,2),
        in p_descripcion varchar(80),
        in p_stock int)
			begin
				update Productos P
					set
						P.nombre = p_nombre,
                        P.precio = p_precio,
                        P.descripcion = p_descripcion,
                        P.stock = p_stock
					where P.idProducto = p_idProducto;
            end//
delimiter ;

delimiter //
	create procedure sp_eliminarProductos(in p_idProducto int)
		begin
			update Productos P
            set
				P.estado = "Sin Existencias",
                P.stock = 0
			where P.idProducto = p_idProducto;
        end//
delimiter ;

-- ===================Compras============================

delimiter //
	create procedure sp_listarComprasPorCliente(in p_idCliente int)
		begin
			select
				C.idCompra,
                C.idCliente,
                C.fechaCompra,
                C.idProducto,
                C.cantidad,
                C.subtotal
            from Compras C
            where C.idCliente = p_idCliente;
        end//
delimiter ;

delimiter //
	create procedure sp_agregarCompras(
        in p_idCliente int,
        in p_idProducto int,
        in p_cantidad int)
			begin
				declare subtotalCalculado decimal(10,2);
                declare precioUnitario decimal(10,2);

                select precio into precioUnitario
                from Productos P
                where P.idProducto = p_idProducto;

                set subtotalCalculado = precioUnitario * p_cantidad;
					insert into Compras(idCliente, fechaCompra, idProducto, cantidad, subtotal)
                    values(p_idCliente, current_date(), p_idProducto, p_cantidad, subtotalCalculado);
            end//
delimiter ;


delimiter //
	create procedure sp_actualizarCompras(
		in p_idCompra int,
        in p_idCliente int,
        in p_idProducto int,
        in p_cantidad int)
			begin
				declare subtotalCalculado decimal(10,2);
                declare precioUnitario decimal(10,2);

                select precio into precioUnitario
                from Productos P
                where p.idProducto = p_idProducto;

                set subtotalCalculado = precioUnitario * p_cantidad;

				update Compras C
					set
						C.fechaCompra = current_date() ,
                        C.idCliente = p_idCliente,
                        C.idProducto = p_idProducto ,
                        C.cantidad = p_cantidad,
                        C.subtotal = subtotalCalculado
					where C.idCompra = p_idCompra;
            end//
delimiter ;

delimiter //
	create procedure sp_cancelarCompra(in p_idCompra int)
		begin
			delete from Compras C
            where C.idCompra = p_idCompra;
        end//
delimiter ;


-- ===========================Triggers==================================
-- =================Restar stock comprado a Productos===================

delimiter //
	create trigger tr_productos_after_insert
    after insert on Compras
    for each row
		begin
			update Productos
				set
					stock = stock - new.cantidad
				where idProducto = new.idProducto;
        end//
delimiter ;

-- ===========Triger que limita la compra de productos si ya no hay stock===================

delimiter //
	create trigger tr_compras_before_insert
    before insert on Compras
    for each  row
		begin
			declare stockActual int;

            select stock into stockActual
            from Productos P
            where p.idProducto = new.idProducto;

            if stockActual <= 0 then
				signal sqlstate '45000'
                set message_text = 'Error: El producto no tiene suficiente stock.';
			end if;
        end//
delimiter ;

-- ======================Trigger que restaura stock despues de cancelar compra===================

delimiter //
	create trigger tr_productos_after_delete
    after delete on Compras
    for each row
		begin
			update Productos
				set
					stock = stock + old.cantidad
				where idProducto = old.idProducto;
        end//
delimiter ;



-- =================Productos por defecto=====================
-- Inserción de 15 carros de ejemplo usando sp_agregarProductos

call sp_agregarProductos('Toyota Corolla', 22500.00, 'Sedán compacto, eficiente y confiable', 15);
call sp_agregarProductos('Honda Civic', 23800.00, 'Sedán deportivo con excelente rendimiento', 12);
call sp_agregarProductos('Ford F-150', 35000.00, 'Camioneta robusta y potente para trabajo pesado', 8);
call sp_agregarProductos('Tesla Model 3', 42000.00, 'Vehículo eléctrico de alto rendimiento y tecnología avanzada', 7);
call sp_agregarProductos('BMW 3 Series', 45000.00, 'Sedán de lujo con diseño elegante y gran potencia', 5);
call sp_agregarProductos('Mercedes-Benz C-Class', 48000.00, 'Sedán premium con confort y sofisticación', 6);
call sp_agregarProductos('Hyundai Tucson', 28000.00, 'SUV compacto, versátil y con buen equipamiento', 10);
call sp_agregarProductos('Kia Sportage', 27500.00, 'SUV moderno con estilo y gran espacio interior', 9);
call sp_agregarProductos('Nissan Sentra', 20500.00, 'Sedán económico y confiable para el día a día', 18);
call sp_agregarProductos('Chevrolet Silverado', 38000.00, 'Camioneta grande con gran capacidad de carga', 7);
call sp_agregarProductos('Jeep Wrangler', 32000.00, 'SUV todoterreno icónico y aventurero', 4);
call sp_agregarProductos('Subaru Outback', 30000.00, 'Crossover familiar, seguro y con tracción AWD', 11);
call sp_agregarProductos('Mazda CX-5', 29000.00, 'SUV elegante con manejo dinámico y eficiente', 13);
call sp_agregarProductos('Volkswagen Jetta', 21500.00, 'Sedán europeo con diseño clásico y confortable', 14);
call sp_agregarProductos('Audi A4', 47000.00, 'Sedán de lujo con tecnología avanzada y rendimiento superior', 5);

```

## Ejecución del Proyecto

1. Clona el repositorio o descarga el código fuente.
2. Asegúrate de tener configurado tu entorno (Java y Maven).
3. Crea la base de datos con el script anterior.
4. Configura las credenciales de la conexión en la clase `Conexion.java`:

```java
private static final String URL = "jdbc:mysql://127.0.0.1:3306/VentaCarrosDB?userSSL=False";
private static final String USER = "tu_usuario";
private static final String PASSWORD = "tu_contraseña";
```

5. Ejecuta con Maven o desde tu IDE:

```bash
clean javafx:run
```

---

## Primeros Pasos en la Aplicación

1. Registrarse como nuevo cliente.
2. Iniciar sesión con los datos proporcionados.
3. Acceder a las diferentes funcionalidades del sistema:

   - Registrar productos.
   - Realizar compras.
   - Gestionar información de clientes y productos.

---

## Estructura del Proyecto (Simplificada)

```
ventaCarros/
├── src/
│   ├── main/java/org/ventacarros
│   │   ├── controller/
│   │   ├── database/
│   │   ├── model/
│   │   └── system/
│   ├── resources/view/
│   └── resources/css/
├── pom.xml
└── README.md
```

---

## Autor

- Proyecto académico desarrollado por \Alessandro Zacarias \Javier Gomez \Miguel Tamat, para fines educativos.

---
