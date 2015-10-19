(ns deepfractal.image
    (:require [deepfractal.utils :as utils]
              [deepfractal.math.mandel :as mandel]
              [deepfractal.math.mandel-bignumber :as mandel-big]
              [deepfractal.math.color :as math-color]))


(defn value->color [v]
  (if (nil? v) 0 255)
  )

(defn make-image [ctx {:keys [width height]} data color-paths]
  (let [img (.createImageData ctx width height)
        img-data (.-data img)
        color-trees (mapv (fn [color]
                            (math-color/priority-map->search-tree
                             (color color-paths)
                             (if (= color :alpha) 255 0)))
                        [:red :green :blue :alpha])
        [r-tree g-tree b-tree a-tree] color-trees]
    (dotimes [j height]
      (let [offset (* j width 4)
            aj (aget data j)]
        (dotimes [i width]
          (let [v (aget aj i)
                o (+ offset (* 4 i))]
            (aset img-data o (math-color/fast--value->color r-tree v))
            (aset img-data (+ o 1) (math-color/fast--value->color g-tree v))
            (aset img-data (+ o 2) (math-color/fast--value->color b-tree v))
            (aset img-data (+ o 3) (math-color/fast--value->color a-tree v))))))
    img))

(defn draw-image [canvas-size {:keys [center zoom max-n] :as fractal-params} color-paths]
  (prn "(draw-image)" center zoom max-n canvas-size)
  (when-let [ctx (some-> js/document
                         (.getElementById "fractal-canvas")
                         (.getContext "2d"))]
    (let [data (time (mandel/make-mandel-data canvas-size center zoom max-n))
          img (time (make-image ctx canvas-size data color-paths))]
      (.putImageData ctx img 0 0))))
