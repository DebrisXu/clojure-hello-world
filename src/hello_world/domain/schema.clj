(ns hello-world.domain.schema
  (:require
    [schema.core :as s]
    ))

  (s/defschema Response
      {:isSuccess s/Bool
      :errcode s/Int
      :errmsg s/Str
      :result {s/Keyword s/Any}})

