(ns modern-cljs.service.userservice
  (:require
    [modern-cljs.service.sessionservice :as sessionservice]
    [modern-cljs.repository.userrepository :as userrepository]
    [modern-cljs.repository.crudrepository :as crudrepository]
    ))

(defn register [user]
  (crudrepository/add userrepository/userRepositoryComponent user))
