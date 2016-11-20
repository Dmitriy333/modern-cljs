(ns modern-cljs.controller.registrationcontroller
  (:require
    [modern-cljs.service.sessionservice :as sessionService]
    [modern-cljs.service.userservice :as userservice]
    [modern-cljs.model.model :as model]
    [ring.util.response :as response]
    [digest :as digest]
    )
  )

(defn registerUser [request]
  (let [registrationParams (:params request)]
    (userservice/register (model/->User nil (:email registrationParams) (:login registrationParams) (digest/md5 ( :password registrationParams)))))
  (response/redirect "/"))
