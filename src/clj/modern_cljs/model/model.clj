(ns modern-cljs.model.model)

(defrecord News [id, title, short-text, full-text, creation-date])

(defrecord User [id, email, login, password])

(defrecord Comment [id, text, creation_date, news_id, user_id])