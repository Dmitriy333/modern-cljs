(ns modern-cljs.model.model)

(defrecord News [id, title, short_text, full_text, creation_date])

(defrecord User [id, email, login, password])

(defrecord Comment [id, text, creation_date, news_id, user_id])

(defrecord Log [id, value, type, creation_date])

(defrecord Audit [id, news_id, user_id, creation_date])

(defrecord Rule [id, entity, entity_event, task])