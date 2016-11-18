(ns modern-cljs.model.model)

(defrecord News [id, title, short-text, full-text, creation-date])

(defrecord User [id, email, login, password])