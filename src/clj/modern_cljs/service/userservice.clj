(ns modern-cljs.service.userservice
  (:require
    [modern-cljs.service.sessionservice :as sessionservice]
    [modern-cljs.repository.userrepository :as userrepository]
    [modern-cljs.repository.crudrepository :as crudrepository]
    ))

(defn register [user]
  (crudrepository/add userrepository/userRepositoryComponent user))

(defn getAuthenticatedUserRole [request]
  (get-in request [:session :role]))

(defn getAuthenticatedUserLogin [request]
  (get-in request [:session :login]))

(defn getAuthenticatedUserEmail [request]
  (get-in request [:session :email]))

(defn isUserLoggedIn? [request]
  (not-empty (getAuthenticatedUserLogin request)))