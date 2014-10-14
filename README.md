BacketDB
========

BacketDB is database that provide powerfull mechanism to manage collections of objects. 
It is accessiable through REST api.

## API

```
GET / - returns list of backet ids.

```

```
GET /:id - returns full backet object.

```

```
GET /:id/aggregates - returns aggregated values for requested backet.

```

```
POST / - create new backet.

```

```
POST /:id/item - add item to backet.

```

```
DELETE /:id - remove backet.

```
