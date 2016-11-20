(ns modern-cljs.repository.crudrepository)

(defprotocol CrudRepository
  (find-all [this])
  (find-by-id [this id])
  (add [this model])
  (delete [this id]))