
package firrtl
package transforms

import firrtl.annotations._
import firrtl.passes.PassException

/** Indicate that DCE should not be run */
case object NoDCEAnnotation extends NoTargetAnnotation

/** A component that should be preserved
  *
  * DCE treats the component as a top-level sink of the circuit
  */
case class DontTouchAnnotation(target: ComponentName) extends SingleTargetAnnotation[ComponentName] {
  def duplicate(n: ComponentName) = this.copy(n)
}

object DontTouchAnnotation {
  class DontTouchNotFoundException(module: String, component: String) extends PassException(
    s"Component marked DONT Touch ($module.$component) not found!\n" +
    "Perhaps it is an aggregate type? Currently only leaf components are supported.\n" +
    "Otherwise it was probably accidentally deleted. Please check that your custom passes are not" +
    "responsible and then file an issue on Github."
  )

  def errorNotFound(module: String, component: String) =
    throw new DontTouchNotFoundException(module, component)
}

/** An [[firrtl.ir.ExtModule]] that can be optimized
  *
  * Firrtl does not know the semantics of an external module. This annotation provides some
  * "greybox" information that the external module does not have any side effects. In particular,
  * this means that the external module can be Dead Code Eliminated.
  *
  * @note Unlike [[DontTouchAnnotation]], we don't care if the annotation is deleted
  */
case class OptimizableExtModuleAnnotation(target: ModuleName) extends
    SingleTargetAnnotation[ModuleName] {
  def duplicate(n: ModuleName) = this.copy(n)
}
