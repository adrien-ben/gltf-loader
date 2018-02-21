package com.adrienben.tools.gltf.validation

/**
 * Base interface for matchers.
 */
interface Matcher<in T> {

    /**
     * Test if [value] matches the matcher.
     */
    fun matches(value: T): Result
}

/**
 * Result of a match test.
 */
class Result(val success: Boolean, val message: String)

/**
 * Exception thrown in case of validation error.
 */
class ValidationError(val field: String, message: String) : Throwable("$field $message")

/**
 * Infix extension that checks the receiver against [matcher].
 *
 * [fieldName] will be append at the beginning of the error message
 * int case of failure.
 *
 * @throws ValidationError if the test does not pass.
 */
fun <T> T.should(matcher: Matcher<T>, fieldName: String = "field") = this.apply {
    val result = matcher.matches(this)
    if (!result.success) throw ValidationError(fieldName, "is $this but should ${result.message}")
}

/**
 * Utility object to get matchers.
 */
object Matchers {

    /**
     * Return an instance of [EqualToMatcher].
     */
    fun <T> equalTo(value: T) = EqualToMatcher(value)

    /**
     * Return an instance of [AtLeastMatcher].
     */
    fun <T : Comparable<T>> atLeast(min: T) = AtLeastMatcher(min)

    /**
     * Return an instance of [GreaterThanMatcher].
     */
    fun <T : Comparable<T>> greaterThan(min: T) = GreaterThanMatcher(min)

    /**
     * Return an instance of [InRangeMatcher].
     */
    fun <T : Comparable<T>> inRange(range: ClosedRange<T>) = InRangeMatcher(range)

    /**
     * Return an instance of [OneOfCollectionMatcher].
     */
    fun <T> oneOf(collection: Collection<T>) = OneOfCollectionMatcher(collection)

    /**
     * Return an instance of [EmptyCollectionMatcher].
     */
    fun <T : Collection<*>> empty() = EmptyCollectionMatcher<T>()

    /**
     * Return an instance of [HaveSizeMatcher].
     */
    fun <T : Collection<*>> haveSize(size: Int) = HaveSizeMatcher<T>(size)

    /**
     * Return an instance of [EmptyMapMatcher].
     */
    fun <T : Map<*, *>> anEmptyMap() = EmptyMapMatcher<T>()

    /**
     * Return an instance of [BeMatcher].
     */
    fun <T> be(matcher: Matcher<T>) = BeMatcher(matcher)

    /**
     * Return an instance of [NotMatcher].
     */
    fun <T> not(matcher: Matcher<T>) = NotMatcher(matcher)
}

/**
 * Equality matcher.
 */
class EqualToMatcher<in T>(private val value: T) : Matcher<T> {

    /**
     * Test that [value] is equal to the matcher's value.
     */
    override fun matches(value: T) = Result(value == this.value, "equal to ${this.value}")
}

/**
 * Matcher to check that a value is greater than or equal to minimum.
 */
class AtLeastMatcher<in T : Comparable<T>>(private val min: T) : Matcher<T> {

    /**
     * Test that [value] is greater than or equal to [min].
     */
    override fun matches(value: T) = Result(value >= min, "at least $min")
}

/**
 * Matcher to check that a value is greater than a minimum.
 */
class GreaterThanMatcher<in T : Comparable<T>>(private val min: T) : Matcher<T> {

    /**
     * Test that [value] is greater than [min].
     */
    override fun matches(value: T) = Result(value > min, "greater than $min")
}

/**
 * Matcher to check that a value in contained in a range.
 */
class InRangeMatcher<in T : Comparable<T>>(private val range: ClosedRange<T>) : Matcher<T> {

    /**
     * Test that [value] is in [range].
     */
    override fun matches(value: T) = Result(value in range, "in $range")
}

/**
 * Matcher to check that some value is contained in an collection.
 */
class OneOfCollectionMatcher<in T>(private val collection: Collection<T>) : Matcher<T> {

    /**
     * Test that [value] is contained in [collection].
     */
    override fun matches(value: T) = Result(collection.contains(value), "one of $collection")
}

/**
 * Matcher to check that a collection is empty.
 */
class EmptyCollectionMatcher<in T : Collection<*>> : Matcher<T> {

    /**
     * Test that [value] is empty.
     */
    override fun matches(value: T) = Result(value.isEmpty(), "empty")
}

/**
 * Matcher to check that a map is empty.
 */
class EmptyMapMatcher<in T : Map<*, *>> : Matcher<T> {

    /**
     * Test that [value] is empty.
     */
    override fun matches(value: T) = Result(value.isEmpty(), "empty")
}

/**
 * Matcher to check that a collection have a certain size.
 */
class HaveSizeMatcher<in T : Collection<*>>(private val size: Int) : Matcher<T> {

    /**
     * Test that [value] has size [size].
     */
    override fun matches(value: T) = Result(value.size == size, "have size $size")
}

/**
 * Matcher to check that another matcher matches. It is just use to compose matchers as sentences.
 */
class BeMatcher<in T>(private val matcher: Matcher<T>) : Matcher<T> {

    /**
     * Test that [value] matches [matcher].
     */
    override fun matches(value: T) = matcher.matches(value).let {
        Result(it.success, "be ${it.message}")
    }
}

/**
 * Matcher to check that another matcher does not match.
 */
class NotMatcher<in T>(private val matcher: Matcher<T>) : Matcher<T> {

    /**
     * Test that [value] does not match [matcher].
     */
    override fun matches(value: T) = matcher.matches(value).let {
        Result(!it.success, "not ${it.message}")
    }
}
