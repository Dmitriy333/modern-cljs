(ns modern-cljs.service.authenticationservice
  (:require [ring.util.response :as response]
            [digest :as d]
            [modern-cljs.repository.userrepository :as userrepo]
            [modern-cljs.views.views :as views]
            [modern-cljs.views.layout :as layout]
            ))

(defn add-user-attribute-to-response [response request authdetails]
  (assoc response :session (-> (:session request)
                               (assoc :role (:role authdetails))
                               (assoc :login (:login authdetails))
                               (assoc :email (:email authdetails))
                               )))

(defn login [request]
  (let [email (get-in request [:params :email])
        password (get-in request [:params :password])]
    (let [user (userrepo/find-by-email-and-hash userrepo/userRepositoryComponent email (d/md5 password))]
      (if (empty? user)
        (layout/application "Incorrect credentials" (views/login-view))
        (add-user-attribute-to-response (response/redirect "/") request {:login (:login user) :role (:role user) :email (:email user)})))))

(defn logout [request]
  (add-user-attribute-to-response (response/redirect "/") request {}))

