(ns arrival.client
  (:require [clojure.edn]
            [clojure.pprint]))

(defn add-element [tag]
  (->> (.createElement js/document tag)
       (.appendChild js/document.body)))

(defn fetch-edn [uri callback]
  (-> (js/window.fetch uri)
      (.then #(.text %))
      (.then #(-> %
                  clojure.edn/read-string
                  callback))))

(defn add-button [id caption on-click]
  (let [button (add-element "button")]
    (set! (.-id button) id)
    (set! (.-textContent button) caption)
    (.addEventListener button "click" on-click)
    button))

(defn add-canvas [id]
  (let [canvas (add-element "canvas")
        style (.-style canvas)]
    (set! (.-id canvas) id)
    (set! (.-height canvas) 300)
    (set! (.-width canvas) 700)
    (set! (.-display style) "block")
    (set! (.-marginTop style) "10px")
    (set! (.-border style) "1px solid #CCC")
    canvas))

(defn add-pre [id content]
  (let [p (add-element "pre")
        style (.-style p)]
    (set! (.-id p) id)
    (set! (.-innerHTML p) content)
    (set! (.-maxHeight style) "30vh")
    (set! (.-overflowY style) "auto")
    p))

(defn draw-chart [data]
  ;; TODO: implement some tricky logic that draws column chart for population of 4 most populous areas in the city.
  (js/console.log "I don't do much yet."))

(defn ^:export app []
  (fetch-edn "/data"
             (fn [edn]
               (add-pre "pop-edn" (with-out-str (cljs.pprint/pprint edn)))
               (add-button "draw" "Draw Column Chart" (fn [] (draw-chart edn)))
               (add-canvas "canvas"))))