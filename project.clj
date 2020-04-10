(defproject hello-world "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 ;[prismatic/schema "1.1.12"]
                 [clj-http "3.10.0"]
                 [metosin/compojure-api "1.1.11"]
                 [com.novemberain/monger "3.1.0"]
                 [cprop "0.1.16"]
                 [cheshire "5.10.0"]
                 [ring/ring-devel "1.7.1"]
                 ]
  :plugins [[lein-ring "0.12.5"]
            [lein-cloverage "1.1.1"]]
  :ring {:handler hello-world.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]
         :resource-paths ["resources/public/dev"]}
   })
