(ns arrival.server
  (:require [org.httpkit.server :as http-kit]
            [cljs.closure]
            [hiccup.core :as hiccup]
            [arrival.data]))

(def index-page
  (let [script (cljs.closure/build "src" {:optimizations :whitespace})]
    (hiccup/html [:html
                  [:head
                   [:meta {:charset "utf-8"}]
                   [:script script]]
                  [:body
                   [:script "arrival.client.app()"]]])))

(defn handler
  [req]
  (case (:uri req)
    "/" {:status  200
         :headers {"Content-Type" "text/html"}
         :body    index-page}
    "/data" {:status  200
             :headers {"Content-Type" "application/edn"}
             :body    (pr-str (arrival.data/flatten-enrich-data arrival.data/population))}
    {:status 404
     :headers {"Content-Type" "text/plain"}
     :body "Not found"}))

(defn -main
  []
  (http-kit/run-server handler {:port 9510}))