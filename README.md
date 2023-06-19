# lixbox-report

Ce dépôt contient un µ-service de reporting avec génération de document.

## Utilisation en java

exemple de code pour un template DOCX avec une sortie au format DOCX

    try
    (   
        InputStream in = ReportServiceBean.class.getResourceAsStream("/template/B1_FAL1_template.docx");
    )
    {   
        ResteasyClient client = (ResteasyClient)ClientBuilder.newClient();
        ResteasyWebTarget target = client.target("http://localhost:19110/report/api");
        ReportService reportService = target.proxy(ReportService.class);
        Document template = new Document();
        template.setMimeType(Constant.DOCX_MIME_TYPE);
        template.setContent(IOUtils.toByteArray(in));
        
        List<DocumentField> fields = new ArrayList<>();
        fields.add(new DocumentField("folder", "test"));
        fields.add(new DocumentField("XA","X"));
        fields.add(new DocumentField("XD", ""));
        fields.add(new DocumentField("name_type", "mon navire"));
        fields.add(new DocumentField("imo_number", "IMO1234586"));
        fields.add(new DocumentField("call_sign", "123CALL"));
        fields.add(new DocumentField("voyage_number", "VN_1234"));
        fields.add(new DocumentField("port_call", "Arrival : FRMRS"));
        fields.add(new DocumentField("flag_state", "FR"));
        fields.add(new DocumentField("date_eta", "12/12/2018 12:30"));
        fields.add(new DocumentField("date_etd", "12/12/2018 12:30"));            
        
        //verif report docx to docx
        Document report = reportService.generateDocument(Langue.FR_FR, template, fields, Constant.DOCX_MIME_TYPE);
        FileUtils.writeByteArrayToFile(new File("./test_r.docx"), report.getContent());
    }
    catch (Exception e)
    {
        LOG.fatal(e);
    }  

exemple de code pour un template DOCX avec une sortie au format PDF

    try
    (   
        InputStream in = ReportServiceBean.class.getResourceAsStream("/template/B1_FAL1_template.docx");
    )
    {   
        ResteasyClient client = (ResteasyClient)ClientBuilder.newClient();
        ResteasyWebTarget target = client.target("http://localhost:19110/report/api");
        ReportService reportService = target.proxy(ReportService.class);        
        Document template = new Document();
        template.setMimeType(Constant.DOCX_MIME_TYPE);
        template.setContent(IOUtils.toByteArray(in));
        
        List<DocumentField> fields = new ArrayList<>();
        fields.add(new DocumentField("folder", "test"));
        fields.add(new DocumentField("XA","X"));
        fields.add(new DocumentField("XD", ""));
        fields.add(new DocumentField("name_type", "mon navire"));
        fields.add(new DocumentField("imo_number", "IMO1234586"));
        fields.add(new DocumentField("call_sign", "123CALL"));
        fields.add(new DocumentField("voyage_number", "VN_1234"));
        fields.add(new DocumentField("port_call", "Arrival : FRMRS"));
        fields.add(new DocumentField("flag_state", "FR"));
        fields.add(new DocumentField("date_eta", "12/12/2018 12:30"));
        fields.add(new DocumentField("date_etd", "12/12/2018 12:30"));            
                
        //verif report docx to docx
        Document report = reportService.generateDocument(Langue.FR_FR, template, fields, Constant.PDF_MIME_TYPE);
        FileUtils.writeByteArrayToFile(new File("./test_r.pdf"), report.getContent());
    }
    catch (Exception e)
    {
        LOG.fatal(e);
    } 

## Installation

Pour utiliser le service, il existe deux modes:

### Déploiement as service:

    java '-Dquarkus.log.handler.gelf.enabled=false' -jar lixbox-report-core-<version.rjar
    
### Déploiement as container

    docker run -eQUARKUS_LOG_HANDLER_GELF_ENABLED=false -p18111:18110 lixboxteam/lixbox-report-core:latest
  

## Dépendances
### API
* Peut s'enregistrer dans un annuaire de service lixbox-registry.


## Configuration

Les paramètres suivants servent à configurer le service:

* **registry.uri**: URI du service d'annuaire initialisée avec **http://main.host:18100/registry/api/1.0**
* **quarkus.http.port**: Port exposée par le service initialisé avec **18110**
* **quarkus.log.file.enable**: Activer le log dans un fichier initialisé avec **true**
* **quarkus.log.file.path**: Chemin du fichier de log initialisé avec **/var/log/lixbox/lixbox-report-core.log**
* **quarkus.log.file.level**: Niveau de log initialisé avec **INFO**
* **quarkus.log.file.format**: Format de log initialisé avec **%d{YYYY/MM/dd HH:mm:ss} %-5p traceId=%X{traceId} %c (%t) %s%e%n**
* **quarkus.log.handler.gelf.enabled**: Activer le log dans le logger GELF initialisé avec **false**
* **quarkus.log.handler.gelf.host**: Host du logger GELF initialisé avec **vsrvglog.lan**
* **quarkus.log.handler.gelf.port**: Port du logger GELF initialisé avec **12201**
* **report.debug.infile.mode**: Enregistrement du template et du resultat dans un fichier sur disque initialisé avec **false**

Ils sont aussi accessibles avec des variables d'environnement

* **REGISTRY_URI**: URI du service d'annuaire initialisée avec **http://main.host:18100/registry/api/1.0**
* **QUARKUS_HTTP_PORT**: Port exposée par le service initialisé avec **18110**
* **QUARKUS_LOG_FILE_ENABLE**: Activer le log dans un fichier initialisé avec **true**
* **QUARKUS_LOG_FILE_PATH**: Chemin du fichier de log initialisé avec **/var/log/lixbox/lixbox-report-core.log**
* **QUARKUS_LOG_FILE_LEVEL**: Niveau de log initialisé avec **INFO**
* **QUARKUS_LOG_FILE_FORMAT**: Format de log initialisé avec **%d{YYYY/MM/dd HH:mm:ss} %-5p traceId=%X{traceId} %c (%t) %s%e%n**
* **QUARKUS_LOG_HANDLER_GELF_ENABLED**: Activer le log dans le logger GELF initialisé avec **false**
* **QUARKUS_LOG_HANDLER_GELF_HOST**: Host du logger GELF initialisé avec **vsrvglog.lan**
* **QUARKUS_LOG_HANDLER_GELF_PORT**: Port du logger GELF initialisé avec **12201**
* **REPORT_DEBUG_INFILE_MODE**: Enregistrement du template et du resultat dans un fichier sur disque initialisé avec **false**


## Utilisateur nécessaire

Sans objet


## Profil accepté par défaut

Sans objet


## Rôles disponibles pour le(s) service(s)

Sans objet

## Fonctions de développement

* [Dev-ui - /q/dev](http://localhost:18110/q/dev)
* [Health-ui - /q/health-ui](http://localhost:18110/q/health-ui)
* [Swagger-ui - /q/swagger-ui](http://localhost:18110/q/swagger-ui)

## Fonctions d'administration

### Healthcheck

* [health-live - /q/health/live](http://localhost:18110/report/api/1.0/health/live)
* [health-ready - /q/health/ready](http://localhost:18110/report/api/1.0/health/ready)

### Performance & Management

* [Metrics - /q/metrics](http://localhost:18110/q/metrics)
* [OpenApi - /q/openapi](http://localhost:18110/q/openapi)

## Database

Sans objet