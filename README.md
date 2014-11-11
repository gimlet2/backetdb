BacketDB
========

[ ![Codeship Status for gimlet2/backetdb](https://codeship.com/projects/bee7a570-4c22-0132-c636-4271b409cf9f/status)](https://codeship.com/projects/46985)

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
