# unwheezeServer
Real-time server backend for the Unwheeze project develloped by Paul Ribeiro and Mehanna Naif.

## Phases de dévellopement :
  1. Pré-étude et architecture de base du projet :
    
    Le code dévellopé sera réalisé en Java et Kotlin en majorité. Le but est de faire persister un coté temps-réel qui permet d'envoyer les données sur les clients connectés en temps réel, mais également en recevoir et les traiter sur le coup. A ses cotés sera dévellopé une API REST qui communiquera avec l'API real time et s'occupera des opérations qui ne requierent pas le temps réel, à savoir, l'inscription, les données statiques, les informations de profil...
    La base de données choisie est RethinkDb, qui dispose d'un coté temps réel et est open source. Elle dispose d'une communauté active et présente des nombreux avantages par rapport a MongoDb. Le choix d'une base de données NoSQL est porté en raison du caractère très changeant des données récoltés et par la nescessité d'un parcours rapide de ces informations.
  Le cluster de communication choisi est MQTT pour les informations transitant du uP au serveur et AMQP pour les clients android et web vers le serveur. Nous utiliserons RabbitMQ afin de dévelloper ces deux parties.
  
  Ainsi, le serveur permettra de stocker les informations relatives aux locations et à la qualité de l'air dans ces locations, les informations des utilisateurs, et les informations liées à la carte.

Enfin, il est nescessaire d'implémenter les algorithmes de recherche sur ce serveur ou un autre serveur à part ( OPTIONNEL ) 
