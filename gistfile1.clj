(ns hello-world.hello)

(defn- clj-map->js-map
  "makes a javascript map from a clojure one"
  [cljmap]
  (let [out (js-obj)]
    (doall (map #(aset out (name (first %)) (second %)) cljmap))
    out))

(defn- attr [object attributes]
  (.attr object (clj-map->js-map attributes)))

(def circs (atom []))

(defn ^:export drawCirc []
  ;;  (draw)
  (let [paper (js/Raphael 0 0 500 500)
        col (.getColor js/Raphael)]
    (reset! circs
            (doall (for [x (range 100)]
                     (let [circ (.circle paper (* x 10) (* x 10) 10)]
                       (attr circ {:fill col})
                       (.drag circ
                              (fn [dx dy x y e]

                                (attr circ {:cx x :cy y}))
                              nil
                              nil)
                       (.mouseover circ #(do
                                           (swap! circs
                                                  (doall (map
                                                          (fn [circ]
                                                            (attr circ {:cx (rand 500) :cy (rand 500)})
                                                            circ)
                                                          @circs)))
                                           (.animate circ (clj-map->js-map {:fill-opacity .5}) 50)))

                       (.mouseout circ #(.animate circ (clj-map->js-map {:fill-opacity 1}) 50))))))))
