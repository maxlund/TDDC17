
;; This is a small problem instance for the standard Logistics domain,
;; as defined in "logistic.pddl".

(define (problem C3_2)
  (:domain logistics)
  (:objects
   room1 room2 room3				;; there are three rooms,
   light1 light2 light3		      		;; one light in each room
   box1		            			;; two boxes
   shakey1      	      			;; shakey
   )
  (:init
   ;; Type declarations:

   ;; box1 and shakey1 are both "objects"
   (object box1) (object shakey1)
   
   ;; shakey1 is of type "shakey"
   (shakey shakey1)

   ;; box1 is of type "box"
   (box box1) 

   ;; all rooms is of type "room", 
   (room room1) (room room2) (room room3)

   ;; and lightswitches as "light".
   (light light1) (light light2) (light light3) 

   ;; "loc" defines the static location for lights
   (loc light1 room1) (loc light2 room2) (loc light3 room3)
   
   ;; doors defines the relation between rooms
   (door_narrow room1 room2)
   (door_narrow room2 room3)
   (door_narrow room2 room3)

   ;; The actual initial state of the problem, which specifies the
   ;; initial locations of all packages and all vehicles:
   (at box1 room1)
   (at shakey1 room1)
   )

   ;; The goal of the problem:
   (:goal (and (at shakey1 room2) (at box1 room2)))
   ;;(:goal (and (at shakey1 room2) (at box1 room3) (switch light1) (switch light2) (switch light3)))
  )

