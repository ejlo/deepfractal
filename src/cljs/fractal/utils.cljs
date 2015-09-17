(ns fractal.utils)

(def debounce-timers (atom {}))

(defn clear-timer! [key]
  (when-let [timeout (@debounce-timers key)]
    (js/clearTimeout timeout)
    (swap! debounce-timers dissoc key)))

(defn debounce [key ms f]
  (clear-timer! key)
  (swap! debounce-timers assoc key
         (js/setTimeout #(do (clear-timer! key) (f)) ms)))
