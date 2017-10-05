(define (domain logistics)
  (:requirements :strips)
  (:predicates

   ;; Static predicates:
   (object ?o)
   (room ?r)
   (shakey ?s)
   (box ?b)
   (light ?l)
   (loc ?l ?r)
   (door_wide ?r1 ?r2)
   (door_narrow ?r1 ?r2)

   ;; Non-static predicates:
   (at ?x ?y) ;; object ?x (skakey or box) is at location ?y (room or box)
   (switch ?l) ;; light is on
   )

  ;; Shakeys six Actions: walk, push a box, climbe up and down from a box, turn on and off a light


  ;; To walk from a room to another room, there must be a wide or narrow door between the rooms,
  ;; and shakey must be in the first room from where he is moving.
  (:action walk
    :parameters (?s ?r1 ?r2)
    :precondition (and (or (and (at ?s ?r1) (door_wide ?r1 ?r2)) (and (at ?s ?r1) (door_narrow ?r1 ?r2)))
		       (room ?r1) (room ?r2) (shakey ?s))
    :effect (and (at ?s ?r2) (not (at ?s ?r1))))

  ;; To push a box between the two rooms, there must be a wide door between the rooms,
  ;; and both shakey and the box must be in the room from where the box is pushed.
  (:action push
   :parameters (?s ?r1 ?r2 ?b)
    :precondition (and (shakey ?s)(room ?r1)(room ?r2)(box ?b)
			(door_wide ?r1 ?r2) (at ?s ?r1) (at ?b ?r1))
    :effect (and (at ?s ?r2) (not (at ?s ?r1))(at ?b ?r2) (not (at ?b ?r1))))

  ;; If shakey is in a room with a box, shakey can climb up on the box
  ;; the effect is that shakey is at the box and not at the room.
  (:action climbup
    :parameters (?s ?b ?r)
    :precondition (and (shakey ?s) (box ?b) (room ?r) 
		       (at ?s ?r) (at ?b ?r))
    :effect (and (at ?s ?b)(not (at ?s ?r))))

  ;; If shakey is in a room with a box, shakey can climb down from the box
  ;; the effect is that shakey is at the room that the box is in (and not at the box).
  (:action climbdown
    :parameters (?s ?b ?r)
    :precondition (and (shakey ?s)(room ?r) (box ?b)
		       (at ?b ?r) (at ?s ?b))
    :effect (and (at ?s ?r) (at ?b ?r)(not (at ?s ?b))))

  ;; If shakey is at a box, that is at a room, that has a lightswitch,
  ;; then shakey can turn on that lightswitch.
  (:action turnon
    :parameters (?s ?b ?r ?l)
    :precondition (and (shakey ?s) (box ?b) (room ?r) (light ?l)
		       (at ?s ?b) (at ?b ?r) (loc ?l ?r))
    :effect (switch ?l))

  ;; Likewise, shakey can turn off the lightswitch.
  (:action turnoff
    :parameters (?s ?b ?r ?l)
    :precondition (and (shakey ?s) (box ?b) (room ?r)(light ?l)
		       (at ?s ?b) (at ?b ?r) (loc ?l ?r))
    :effect (not(switch ?l)))
  )
