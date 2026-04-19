# Uso Básico

Esta guía describe los pasos básicos para utilizar TPVNoteERP, desde el inicio de la aplicación hasta la realización de operaciones comunes.

## Inicio de la Aplicación

1. Abre el proyecto en NetBeans o tu IDE preferido.
2. Ejecuta la clase principal `TPVNoteERP.java` ubicada en el paquete `tpvnoteerp`.
3. La aplicación se iniciará y mostrará la ventana de login.

## Inicio de Sesión (Login)

1. En la ventana de login, ingresa tu **nombre de usuario** y **contraseña**.
2. Haz clic en el botón "Iniciar Sesión".
3. Si las credenciales son correctas, accederás al menú principal. El acceso a ciertas secciones depende de tu rol de usuario (por ejemplo, solo los administradores pueden gestionar productos y usuarios).

## Navegación en el Menú Principal

El menú principal consta de varios botones que te permiten acceder a diferentes secciones:

- **Ventas**: Para realizar transacciones de venta.
- **Productos** (solo administradores): Para gestionar el inventario.
- **Clientes**: Para administrar la base de datos de clientes.
- **Usuarios** (solo administradores): Para gestionar cuentas de usuario.
- **Informes** (solo administradores): Para visualizar reportes.

Haz clic en el botón correspondiente para cambiar a la sección deseada.

## Realizar una Venta Básica

1. Ve a la sección **Ventas**.
2. En el panel de productos, selecciona los artículos que deseas vender haciendo clic en ellos.
3. Los productos se agregarán automáticamente al carrito de compras.
4. Si es necesario, selecciona un cliente de la lista desplegable.
5. Revisa el carrito: puedes ajustar cantidades o eliminar productos si es necesario.
6. Una vez listo, haz clic en "Procesar Pago".
7. Selecciona el método de pago (efectivo, etc.) y confirma la transacción.
8. La venta se registrará en la base de datos y se actualizará el stock.

## Gestionar Productos (Solo Administradores)

1. Ve a la sección **Productos**.
2. Utiliza la tabla para ver, agregar, editar o eliminar productos.
3. Para agregar un nuevo producto, haz clic en "Agregar" y completa los campos requeridos.
4. Guarda los cambios para actualizar el inventario.

## Ver Informes (Solo Administradores)

1. Ve a la sección **Informes**.
2. Selecciona el tipo de reporte deseado (ventas por período, stock total, etc.).
3. Los informes se generan utilizando JasperReports y se pueden exportar o imprimir.

## Cerrar Sesión

Para cerrar la sesión, simplemente cierra la aplicación o implementa un botón de logout si está disponible en futuras versiones.

Para soporte adicional, consulta la documentación técnica o contacta al desarrollador.