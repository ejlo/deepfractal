(ns deepfractal.db)

(def default-db
  {:canvas-size {:width 320 :height 200}
   :ctx nil
   :fractal-params {:center [-0.75 0]
                    :max-n 1000
                    :zoom 1}})
