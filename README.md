# projet_WebDatamining

Projet web Datamining 

1 - détection des termes appraissant trop fréquemment dans l'ensemble du corpus ou dans un nombre de textes trop important (DONE)

2 - création d'une liste de déterminants additionnels à supprimer (car n'apportant pas d'informations)

3 - Constitution d'un dictionnaire contenant l'ensemble des termes présent dans le corpus (index) en ayant enlevé de l'index l'ensembles
des mots des points 1 et 2 => pour chaque terme on y associe :
                            - l'ensemble des documents contenant ce terme
                            - la fréquence dans le corpus
(DONE)

3 Bis - suppression de termes dans l'index (DONE)

3 Ter - Ajout de terme dans l'index (avec update de la fréquence du corpsu et des textes contenant le terme) 
                            
4 - Recherche a ffiné dans le corpus en classant les documents par pertinence (à définir : fréquences d'occurence, matrice tf-idf,...)

5 - Eventuellement : rehcerche avec NEAR, gestion du OU exclusif
