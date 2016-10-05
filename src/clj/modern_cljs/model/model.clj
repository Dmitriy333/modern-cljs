(ns modern-cljs.model.model)

(defrecord News [id, short-text, full-text, creation-date])
(defrecord User [id, email, login, password])