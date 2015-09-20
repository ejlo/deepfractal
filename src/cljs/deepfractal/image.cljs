(ns deepfractal.image
    (:require [deepfractal.utils :as utils]
              [deepfractal.math.mandel :as mandel]
              [deepfractal.math.mandel-bignumber :as mandel-big]))


(defn value->color [v]
  (if (nil? v) 0 255)
  )

(defn make-image [ctx {:keys [width height]} data]
  (let [img (.createImageData ctx width height)
        img-data (.-data img)]
    (dotimes [j height]
      (let [offset (* j width 4)
            aj (aget data j)]
        (dotimes [i width]
          (let [col (value->color (aget aj i))]
            (aset img-data (+ offset (* 4 i) 0) col)
            (aset img-data (+ offset (* 4 i) 1) col)
            (aset img-data (+ offset (* 4 i) 2) col)
            (aset img-data (+ offset (* 4 i) 3) 255)))))
    img))

(defn draw-image [canvas-size {:keys [center zoom max-n] :as fractal-params}]
  (prn "(draw-image)" center zoom max-n canvas-size)
  (when-let [ctx (some-> js/document
                         (.getElementById "fractal-canvas")
                         (.getContext "2d"))]
    (let [data (mandel/make-mandel-data canvas-size center zoom max-n)
          img (make-image ctx canvas-size data)]
      (.putImageData ctx img 0 0))))
