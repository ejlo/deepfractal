(ns deepfractal.math.mandel
  (:require [deepfractal.utils :as utils]))


(defn sqr [x] (* x x))

(defn ** [base exponent]
  (.pow js/Math base exponent))

(defn log2 [x]
  (/ (.log js/Math x) (.-LN2 js/Math))
  )

(def limit (** 2 (** 2 5)))

(defn d2->n-value [d2 n]
  (- (+ n 5) (log2 (* 0.5 (log2 d2)))))


(defn trivially-inside? [x y]
  (let [xm (- x 0.25)
        r2 (+ (sqr xm) (sqr y))
        xp (+ x 1.0)
        d2 (+ (sqr xp) (sqr y))]
    (or
     (< (sqr (+ xm (* 2 r2))) r2) ; inside cardioid
     (< d2 0.0625)                ; inside left circle
     )))

(defn mandel-simple [max-n in-x in-y]
  (if (trivially-inside? in-x in-y)
    nil
    (loop [n 0 x 0.0 y 0.0]
      (if (== n max-n)
        nil
        (let [x2 (* x x)
              y2 (* y y)
              d2 (+ x2 y2)]
          (if (> d2 limit)
            (d2->n-value d2 n)
            (recur (inc n)
                   (+ x2 (- y2) in-x)
                   (+ (* 2 x y) in-y))))))))

(def mandel mandel-simple)

(defn make-mandel-data [{:keys [width height]} [cx cy :as center] zoom max-n]
  (let [ds (/ 3.5 (* 0.5 (+ width height)) zoom)
        cx (utils/->float cx)
        cy (utils/->float cy)
        max-n (int max-n)
        a #js []]
    (dotimes [j height]
      (let [y (+ cy (* (- (/ (dec height) 2) j) ds))
            _ (aset a j #js [])
            aj (aget a j)]
        (dotimes [i width]
          (let [x (+ cx (* (- i (/ (dec width) 2)) ds))]
            (aset aj i (mandel max-n x y))))))
    a))

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
    (let [data (make-mandel-data canvas-size center zoom max-n)
          img (make-image ctx canvas-size data)]
      (.putImageData ctx img 0 0))))

(comment

  (mandel-simple 10000 0 1)
  (mandel-simple 35 0 1.0000000001)
  (mandel-simple 36 0 1.0000000001)

  (time
   (let [x 0.000002342]
     (/ (* x (mandel-simple :no-limit -0.75 x))
        (.-PI js/Math))))

  (do
    (defn round2 [x]
      (* 0.01 (.round js/Math (* x 100))))

    (defn get-time! [] (.getTime (js/Date.)))

    (defn benchmark [f n]
      (let [start (get-time!)
            ret (f n -0.1 0.8)
            _ (prn ret)
            ms (- (get-time!) start)
            _ (prn ms)
            Miter-per-s (round2 (/ n ms 1000))]
        Miter-per-s)))

  (do
    (time (def data (make-mandel-data {:width 1920 :height 1080}
                                      [-0.75 0] 1 1000)))
    nil)

  (time
   (let [canvas (.getElementById js/document "fractal-canvas")
         ctx (.getContext canvas "2d")
         dim {:width 329 :height 362}]
     (draw-image ctx dim {:center [-0.158 1.0335] :zoom 15 :max-n 10000})))

  (benchmark mandel-simple 30000000)

  ;; ~289M iterations/s
  )
