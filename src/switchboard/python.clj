(ns switchboard.python
  (:require [switchboard.utils :refer :all]))


;; TODO: try factoring out the common pattern between this and pypi
(def py (partial build-url "https://docs.python.org"))
(defn python [version rest]
  (let [py (partial py version)]
    (if (nil? rest)
      (py "library")
      (let [direct (py "library" (str rest ".html"))]
        (if (exists? direct)
          direct
          (py (str "search.html?q=" rest)))))))

(def py2 (partial python "2.7"))
(def py3 (partial python "3"))
(def py36 (partial python "3.6"))
(def py37 (partial python "3.7"))


(def -pypi (partial build-url "https://pypi.org"))
(defn pypi [rest]
  (if (nil? rest)
    (-pypi)
    (let [direct (-pypi "project" rest)]
      (if (exists? direct)
        direct
        (-pypi (str "search/?q=" rest))))))
