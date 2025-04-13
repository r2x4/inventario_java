# inventario_java

This project is an inventory management system designed for a mobile application. 
It provides functionalities for:
- Entry of products
- Registration of sellers
- Management of sales and stock
- A dedicated folder will contain the CSV files with the data.

## Estructura de Archivos CSV

Los archivos CSV deben estar ubicados en la carpeta `Datos/` y deben tener la siguiente estructura:

### vendedores.csv

*   **id:** Identificador único del vendedor (entero).
*   **nombre:** Nombre del vendedor (texto).
*   **idIdentificacion:** Número de identificación del vendedor (texto).
*   **ciudad:** Ciudad del vendedor (texto).
*   **pais:** País del vendedor (texto).
*   **fechaIngreso:** Fecha de ingreso del vendedor al sistema (formato YYYY-MM-DD).

### productos.csv
*   **id:** Identificador único del producto (entero).
*   **nombre:** Nombre del producto (texto).
*   **marca:** Marca del producto (texto).
*   **valorCompra:** Valor de compra del producto (decimal).
*   **stock:** Cantidad de unidades disponibles en stock (entero).
*   **fechaIngreso:** Fecha de ingreso del producto al sistema (formato YYYY-MM-DD).

### ventas.csv
*   **id:** Identificador único de la venta (entero).
*   **id_vendedor:** Identificador del vendedor asociado a la venta (entero).
*   **id_producto:** Identificador del producto vendido (entero).
*   **cantidad:** Cantidad de unidades vendidas (entero).
*   **precio_venta:** Precio de venta por unidad (decimal).
*   **fecha:** Fecha en que se realizó la venta (formato YYYY-MM-DD).

## Generación de Datos de Ejemplo

Se ha creado la clase `GenerateInfoFiles.java` para generar datos de ejemplo para vendedores, productos y ventas. Estos datos se guardan en los archivos CSV correspondientes dentro de la carpeta `Datos/`.

Para generar los archivos CSV con datos de ejemplo, se debe ejecutar la opción **6** del menú principal de la aplicación. Esto ejecutará el método `generarTodosLosDatos()` de la clase `GenerateInfoFiles.java`, que se encargará de crear los archivos y llenarlos con la información.


