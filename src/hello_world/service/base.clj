(ns hello-world.service.base
  (:require [hello-world.util.response :as res])
  )

(defn hello-world []
  (res/succResponse {:msg "Hello World!!"}))
