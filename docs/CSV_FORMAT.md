# Formato del Archivo CSV

## Estructura Requerida

El archivo CSV debe tener esta estructura:

```csv
id,nombre,email,ciudad
1,Ana López,ana.lopez@email.com,Madrid
2,Carlos Ruiz,carlos.ruiz@email.com,Valencia
```

## Requisitos

- **Primera línea**: Cabecera obligatoria `id,nombre,email,ciudad`
- **Separador**: Coma (`,`)
- **Codificación**: UTF-8
- **Campos**: Todos obligatorios, no pueden estar vacíos

## Especificación de Campos

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Número entero | Identificador único (1, 2, 3...) |
| `nombre` | Texto | Nombre completo del cliente |
| `email` | Texto | Correo electrónico (debe contener @) |
| `ciudad` | Texto | Ciudad de residencia |

## Errores Comunes

**Incorrecto:**
```csv
1;Ana López;ana@email.com;Madrid  (usa ; en vez de ,)
Ana López,ana@email.com,Madrid    (falta cabecera)
1,Ana López,,Madrid                (campo vacío)
```

**Correcto:**
```csv
id,nombre,email,ciudad
1,Ana López,ana@email.com,Madrid
2,Carlos Ruiz,carlos@email.com,Valencia
```

## Ejemplo de Prueba

Puedes usar el archivo incluido: `src/main/resources/clientes.csv`