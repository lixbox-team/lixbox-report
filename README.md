# lixbox-param

Ce dépôt contient le µservice de param
Le site du service est [ici](https://project-site.service.dev.lan/lixbox-param)


## Dépendances
### API
* Nécessite un service Redis.
* Peut s'enregistrer dans un annuaire de service lixbox-registry.  

### UI
* Sans objet

## Configuration 
### API
Les variables d'environnement suivantes servent à configurer le service:
* **REGISTRY_URI**: URI du service d'annauire initialisée avec **http://main.host:18100/registry/api/1.0**
* **PARAM_REDIS_URI**: URI du service Redis initialisée avec **tcp://localhost:6381**
* **QUARKUS_HTTP_PORT**: Port exposée par le service initialisé avec **18102**

### UI
Les variables d'environnement suivantes servent à configurer le service:
* **PARAM_API_URL**: URI du service Redis initialisée avec **http://localhost:18102/param/api/1.0**

## Utilisateur nécessaire

Sans objet


## Profil accepté par défaut

Sans objet


## Rôles disponibles pour le(s) service(s)

Sans objet

## Contrat et URL
### API

### UI



## Fonctions d'administration

Sans objet
     

## Fonctions batch

Sans objet


## Policies JAAS

Sans objet


## Database

Sans objet